package com.pabu5h.pq_decoder.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ExcelUtil {
    Logger logger = Logger.getLogger(ExcelUtil.class.getName());
    public Map<String, Object> createExcelWithMultipleSheets(Map<String, Object> channelMap,String type) throws IOException {
        // Create an Excel workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Write the general info to the first sheet

        List<String> ExcelHeaders;

        List<Map<String, Object>> channels = new ArrayList<>();
        if("comtrade".equals(type)){
            channels = (List<Map<String, Object>>) channelMap.get("analogChannels");
            ExcelHeaders = List.of("stationName", "deviceId", "numOfChannels","lineFrequency","startTimestamp","endTimestamp","samplingRate","numOfSamples");
        }else{
            channels = (List<Map<String, Object>>) channelMap.get("channel_info");
            ExcelHeaders = List.of("file_name", "channel_count", "timestamp","sampling_rate");
        }
        writeGeneralInfoSheet(workbook, channelMap,ExcelHeaders);
        for (int i = 0; i < channels.size(); i++) {
            Map<String, Object> channel = channels.get(i);
            writeChannelInfoSheet(workbook, channel, i + 1);  // Creating sheets from index 1 onwards for channels
        }

        // Convert the workbook to a byte array output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Return the Excel file as a byte array for further processing (e.g., sending to front end)
        return Map.of("result", outputStream.toByteArray());
    }

    private void writeGeneralInfoSheet(XSSFWorkbook workbook, Map<String, Object> channelInfo, List<String> headers) {
        // Create the first sheet for general info
        Sheet sheet = workbook.createSheet("General Info");

        int rowIndex = 0;

        for (String header : headers) {
            Row currentRow = sheet.createRow(rowIndex++);

            // Column 0: Header
            Cell headerCell = currentRow.createCell(0);
            headerCell.setCellValue(header);

            // Column 1: Corresponding value from the map
            Object value = channelInfo.get(header);
            String cellValue = value != null ? value.toString() : "";

            // Truncate if needed
            if (cellValue.length() > 32767) {
                cellValue = cellValue.substring(0, 32740);
            }

            Cell valueCell = currentRow.createCell(1);
            valueCell.setCellValue(cellValue);
        }

        // Set consistent column width
        sheet.setColumnWidth(0, 6000); // Header column
        sheet.setColumnWidth(1, 10000); // Value column
    }

    private void writeChannelInfoSheet(XSSFWorkbook workbook, Map<String, Object> channel, int sheetIndex) {
        String channelName = channel.get("channel_name") != null ? (String)channel.get("channel_name") : "Channel" + sheetIndex;

        // Remove illegal characters
        channelName = channelName.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        if (channelName.isEmpty()) {
            channelName = "Channel";
        }
        // Truncate to ensure total length ≤ 31
        int maxBaseLength = 31;
        if (channelName.length() > maxBaseLength) {
            channelName = channelName.substring(0, maxBaseLength);
        }

        // Ensure uniqueness
        int duplicateCount = 1;
        String originalName = channelName;
        while (workbook.getSheet(channelName) != null) {
            channelName = originalName + "_" + duplicateCount++;
            if (channelName.length() > 31) {
                channelName = channelName.substring(0, 31); // Just in case
            }
        }

        Sheet sheet = workbook.createSheet(channelName);
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);

       //  Header row
        Row headerRow = sheet.createRow(0);
        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("Property");
        headerCell0.setCellStyle(boldStyle);

        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("Value");
        headerCell1.setCellStyle(boldStyle);

        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 30 * 256);

        // Start writing data
        int rowIndex = 1;
        for (Map.Entry<String, Object> entry : channel.entrySet()) {
            if ("values".equals(entry.getKey())) {
                continue;  // Skip 'values' for now; add them in the next section
            }

            Row row = sheet.createRow(rowIndex++);
            String propertyName = entry.getKey();
            String propertyValue = entry.getValue() != null ? entry.getValue().toString() : "";

            // Check the length of the property value and truncate if necessary
            if (propertyValue.length() > 32740) {
                // Ensure the final string INCLUDING "... (truncated)" is max 32767 chars

                propertyValue = propertyValue.substring(0, 32740);
            }

            row.createCell(0).setCellValue(propertyName);
            row.createCell(1).setCellValue(propertyValue);
        }
        sheet.createRow(rowIndex++); // This adds an empty row
        // Now, write the 'values' section
        List<Number> values = (List<Number>) channel.get("values");
        Row valuesHeaderRow = sheet.createRow(rowIndex++);
        valuesHeaderRow.createCell(0).setCellValue("Values");
        if (values.size() > 1_000_000) {
            values = values.subList(0, 1_000_000); // Limit to Excel max
        }
//         Write each value in its own row
        for (Number value : values) {
            Row valueRow = sheet.createRow(rowIndex++);
            String valueString = String.valueOf(value != null ? value : "");

            // Check the length of the value and truncate if necessary
            if (valueString.length() > 32740) {
                valueString = valueString.substring(0, 32740);

            }

            valueRow.createCell(0).setCellValue(valueString);
        }
    }

    public Map<String,Object> convertToZipFile(String operation,String filename,Map<String,Object> data,Map<String,Object> logicalData,String type) throws IOException {
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(zipOutputStream)) {
            String extension;
            String fileName;
            if ("downloadJsonZip".equals(operation)) {
                extension = ".json";

                String jsonContent = new ObjectMapper().writeValueAsString(data);
                fileName = filename + extension;
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(jsonContent.getBytes(StandardCharsets.UTF_8));
            } else {
                extension = ".xlsx";

                Map<String, Object> xlsxResult = createExcelWithMultipleSheets(logicalData, type);
                byte[] xlsxBytes = (byte[]) xlsxResult.get("result");
                fileName = filename + extension;
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(xlsxBytes);
            }

            zipOut.closeEntry();
            zipOut.finish();  // Properly finalize the zip file

            byte[] zipBytes = zipOutputStream.toByteArray();  // Ensure conversion to byte array
            return Map.of("result", zipBytes);

        } catch (IOException e) {
            logger.severe("Error creating JSON zip file");
            return(Map.of("error", "Failed to create JSON zip file"));
        }
    }
}
