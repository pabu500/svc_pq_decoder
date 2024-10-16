package com.pabu5h.comtrade.processor;

import com.pabu5h.comtrade.ComtradeModule;
import com.pabu5h.comtrade.config.ComtradeConfig;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ComtradeProcessor {
    Logger logger = Logger.getLogger(ComtradeProcessor.class.getName());
    private ComtradeModule comtradeModule;
    @Autowired
    public ComtradeProcessor(ComtradeModule comtradeModule) {
        this.comtradeModule = comtradeModule; // Ensure it's being set correctly
    }
    public Map<String, Object> processPqd(InputStream cfgInputStream, InputStream datInputStream, int step, String operation) throws IOException {
        Map<String, Object> result = comtradeModule.processCfgConfig(cfgInputStream, datInputStream, step);
        ComtradeConfig comtradeResult = (ComtradeConfig) result.get("result");
        Map<String, Object> convertedConfigMap = convertConfigFieldsToMap(comtradeResult);
        Map<String, Object> comtradeMap = (Map<String, Object>) convertedConfigMap.get("result");

        switch (operation) {
            case "plotGraph":
                result.put("result", comtradeMap);
                break;
            case "downloadJsonZip":
                result.put("result", comtradeMap);
                break;
            case "downloadCsvZip":
                logger.info("Creating a zip file with CSV and XLSX content");
                ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
                try (ZipOutputStream zipOut = new ZipOutputStream(zipOutputStream)) {

                    String xlsxFileName = "data.xlsx";
                    ByteArrayOutputStream xlsxOutputStream = new ByteArrayOutputStream();
                    Map<String, Object> xlsxResult = createExcelWithMultipleSheets(comtradeMap);
                    xlsxOutputStream.write((byte[]) xlsxResult.get("result")); // Ensure you get the byte array from the result
                    ZipEntry xlsxZipEntry = new ZipEntry(xlsxFileName);
                    zipOut.putNextEntry(xlsxZipEntry);
                    zipOut.write(xlsxOutputStream.toByteArray());
                    zipOut.closeEntry();
                    zipOut.finish(); // Ensure all entries are finished
                    return Map.of("result", zipOutputStream.toByteArray());
                }
            default:
                break;
        }
        result.put("result", comtradeMap);
        return result;
    }

    public Map<String,Object> validateFile(MultipartFile file) {
        // Check if the file has a valid extension (e.g., .cfg or .dat)
        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".cfg") || fileName.endsWith(".dat"))) {
            return Map.of("error","invalid file extension");
        }

        //Check the size of the file
        if (file.getSize() == 0) {
            return Map.of("error","The file " + fileName + " is empty.");
        }

        if (fileName.endsWith(".cfg")) {
            try{
                String content = new String(file.getBytes(), StandardCharsets.UTF_8);
                String[] lines = content.split("\n");
                if (lines.length < 3) { // Example: Check for a minimum line requirement
                    return Map.of("error", "The .dat does not contain enough information.");
                }
            }catch(Exception e){
                return Map.of("error", "The .dat file is not in the correct format.");
            }

        }
        if (fileName.endsWith(".dat")) {
            try{
                byte[] bytes = file.getBytes();
                if (bytes.length < 10) { // Example: Check for a minimum size requirement
                    return Map.of("error", "The .dat file is too small.");
                }
            }catch (Exception e){
                return Map.of("error", "The .dat file is not in the correct format.");
            }

        }

        return Map.of("success",true);
    }

    public Map<String, Object> convertConfigFieldsToMap(ComtradeConfig comtradeConfig) {
        LinkedHashMap<String, Object> configMap = new LinkedHashMap<>();
        configMap.put("stationName", comtradeConfig.getStationName());
        configMap.put("deviceId", comtradeConfig.getDeviceId());
        configMap.put("revYear", String.valueOf(comtradeConfig.getRevYear()));
        configMap.put("numOfChannels", String.valueOf(comtradeConfig.getNumOfChannels()));
        configMap.put("numOfAnalogChannels", String.valueOf(comtradeConfig.getNumOfAnalogChannels()));
        configMap.put("numOfDigitalChannels", String.valueOf(comtradeConfig.getNumOfDigitalChannels()));
        configMap.put("lineFrequency", String.valueOf(comtradeConfig.getLineFrequency()));
        configMap.put("startTimestamp", comtradeConfig.getStartTimestamp());
        configMap.put("endTimestamp", comtradeConfig.getEndTimestamp());
        configMap.put("fileType", comtradeConfig.getFileType());
        configMap.put("timeMultiplier", String.valueOf(comtradeConfig.getTimeMultiplier()));
        configMap.put("numOfSamples", String.valueOf(comtradeConfig.getNumOfSamples()));
        configMap.put("sampleRates",  String.valueOf(comtradeConfig.getSampleRates()));
        configMap.put("analogChannels", comtradeConfig.getAnalogChannels());
        configMap.put("digitalChannels", comtradeConfig.getDigitalChannels());
        return Map.of("result",configMap);
    }


    public Map<String, Object> createExcelWithMultipleSheets(Map<String, Object> comtradeMap) throws IOException {
        // Create an Excel workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Write the general info to the first sheet
        writeGeneralInfoSheet(workbook, comtradeMap);

        // Write analog channels to individual sheets
        List<Map<String, Object>> analogChannels = (List<Map<String, Object>>) comtradeMap.get("analogChannels");
        for (int i = 0; i < analogChannels.size(); i++) {
            Map<String, Object> channel = analogChannels.get(i);
            writeAnalogChannelSheet(workbook, channel, i + 1);  // Creating sheets from index 1 onwards for channels
        }

        // Convert the workbook to a byte array output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Return the Excel file as a byte array for further processing (e.g., sending to front end)
        return Map.of("result", outputStream.toByteArray());
    }

    private void writeGeneralInfoSheet(XSSFWorkbook workbook, Map<String, Object> generalInfo) {
        // Create the first sheet for general info
        Sheet sheet = workbook.createSheet("General Info");


        // Write headers (first row)
        int headerCellIndex = 0;
        List<String> headers = List.of("station name", "device id", "rev year", "num of channels", "num of analog channels", "num of digital channels", "line frequency", "start timestamp", "end timestamp", "file type", "time multiplier", "num of samples");
        int rowIndex = 0; // Start from the first row

        // Get an iterator for the generalInfo values
        Iterator<Object> valueIterator = generalInfo.values().iterator();

        for (String header : headers) {
            Row currentRow = sheet.createRow(rowIndex++); // Create a new row for each header

            // Create and set the header cell in the first column
            Cell headerCell = currentRow.createCell(0); // Column 0 for header
            headerCell.setCellValue(header);

            // Check if there are more values to set
            if (valueIterator.hasNext()) {
                // Get the next value from generalInfo
                Object value = valueIterator.next();
                Cell valueCell = currentRow.createCell(1); // Column 1 for value
                String cellValue = value != null ? value.toString() : "";

                // Check the length of the value and truncate if necessary
                if (cellValue.length() > 32767) {
                    cellValue = cellValue.substring(0, 32767) + "... (truncated)";
                }

                valueCell.setCellValue(cellValue); // Set the value cell
            } else {
                // If no more values, set the second cell to empty
                currentRow.createCell(1).setCellValue(""); // Set empty if no corresponding value
            }
        }
        for (int i = 0; i < 2; i++) {
            sheet.setColumnWidth(i, 6000); // Set each column width to 20 characters
        }
    }


    private void writeAnalogChannelSheet(XSSFWorkbook workbook, Map<String, Object> channel, int sheetIndex) {
        // Create a new sheet for each analog channel
        String channelName = channel.get("name") != null ? channel.get("name").toString() : "Channel " + sheetIndex;
        Sheet sheet = workbook.createSheet(channelName);
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        // Create a cell style with the bold font
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        // Write channel-specific headers and properties (first row)
        Row headerRow = sheet.createRow(0);
        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("Property");
        headerCell0.setCellStyle(boldStyle); // Set the bold style

        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("Value");
        headerCell1.setCellStyle(boldStyle); // Set the bold style
        sheet.setColumnWidth(0, 20 * 256); // Set width for 'Property' column
        sheet.setColumnWidth(1, 30 * 256); // Set width for 'Value' column
        // Write channel properties in the next rows
        int rowIndex = 1;
        for (Map.Entry<String, Object> entry : channel.entrySet()) {
            if ("values".equals(entry.getKey())) {
                continue;  // Skip 'values' for now; add them in the next section
            }

            Row row = sheet.createRow(rowIndex++);
            String propertyName = entry.getKey();
            String propertyValue = entry.getValue() != null ? entry.getValue().toString() : "";

            // Check the length of the property value and truncate if necessary
            if (propertyValue.length() > 32767) {
                propertyValue = propertyValue.substring(0, 32767) + "... (truncated)";

            }

            row.createCell(0).setCellValue(propertyName);
            row.createCell(1).setCellValue(propertyValue);
        }
        sheet.createRow(rowIndex++); // This adds an empty row
        // Now, write the 'values' section
        List<Double> values = (List<Double>) channel.get("values");
        Row valuesHeaderRow = sheet.createRow(rowIndex++);
        valuesHeaderRow.createCell(0).setCellValue("Values");

        // Write each value in its own row
        for (Double value : values) {
            Row valueRow = sheet.createRow(rowIndex++);
            String valueString = String.valueOf(value != null ? value : "");

            // Check the length of the value and truncate if necessary
            if (valueString.length() > 32767) {
                valueString = valueString.substring(0, 32767) + "... (truncated)";

            }

            valueRow.createCell(0).setCellValue(valueString);
        }

    }


    public void createCsv(OutputStream outputStream, Map<String, Object> Comtrade) throws IOException {
        // Create a BufferedWriter to write to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            // Write general info
            writeGeneralInfo(writer, Comtrade);

            // Write a separator for analog channels
            writer.newLine(); // Add a new line before starting analog channels

            // Write analog channels
            List<Map<String, Object>> analogChannels = (List<Map<String, Object>>) Comtrade.get("analogChannels");
            for (Map<String, Object> channel : analogChannels) {
                writeAnalogChannel(writer, channel);
            }

        }
    }

    private void writeGeneralInfo(BufferedWriter writer, Map<String, Object> result) throws IOException {
        // Write headers
        String headers = String.join(",", result.keySet());
        writer.write(headers);
        writer.newLine(); // Move to the next line

        // Write values
        String values = result.values().stream()
                .map(value -> value != null ? value.toString() : "") // Handle nulls
                .reduce((a, b) -> a + "," + b)
                .orElse("");
        writer.write(values);
        writer.newLine(); // Move to the next line
    }

    private void writeAnalogChannel(BufferedWriter writer, Map<String, Object> channel) throws IOException {
        // Write channel-specific headers
        writer.write("Channel Properties for " + channel.get("name")); // Assuming there's a 'name' field
        writer.newLine();
        writer.write("Property,Value");
        writer.newLine(); // Move to the next line

        // Write channel properties
        for (Map.Entry<String, Object> entry : channel.entrySet()) {
            if ("values".equals(entry.getKey())) {
                continue; // Skip values for now; we'll add them later
            }
            writer.write(entry.getKey() + "," + (entry.getValue() != null ? entry.getValue().toString() : "")); // Handle nulls
            writer.newLine(); // Move to the next line
        }

        // Now, add the values header
        writer.write("Values");
        writer.newLine();

        // Retrieve and write values
        List<Double> values = (List<Double>) channel.get("values");
        for (Double value : values) {
            writer.write(value != null ? value.toString() : ""); // Handle nulls
            writer.newLine(); // Move to the next line
        }

        writer.newLine(); // Add a new line after each channel for separation
    }
}
