package com.pabu5h.comtrade.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Map<String, Object> createExcelWithMultipleSheets(Map<String, Object> channelMap,String type,List<String> headers) throws IOException {
        // Create an Excel workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Write the general info to the first sheet
        writeGeneralInfoSheet(workbook, channelMap,headers);

        List<Map<String, Object>> channels = new ArrayList<>();
        if("comtrade".equals(type)){
            channels = (List<Map<String, Object>>) channelMap.get("analogChannels");
        }else{
            channels = (List<Map<String, Object>>) channelMap.get("channel_info");
        }
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

    private void writeGeneralInfoSheet(XSSFWorkbook workbook, Map<String, Object> channelInfo,List<String> headers) {
        // Create the first sheet for general info
        Sheet sheet = workbook.createSheet("General Info");


        // Write headers (first row)
        int headerCellIndex = 0;

        int rowIndex = 0; // Start from the first row

        // Get an iterator for the generalInfo values
        Iterator<Object> valueIterator = channelInfo.values().iterator();

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


    private void writeChannelInfoSheet(XSSFWorkbook workbook, Map<String, Object> channel, int sheetIndex) {
        // Create a new sheet for each analog channel
        String channelName = channel.get("channel_name") != null ? channel.get("channel_name").toString() : "Channel " + sheetIndex;
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
        List<Number> values = (List<Number>) channel.get("values");
        Row valuesHeaderRow = sheet.createRow(rowIndex++);
        valuesHeaderRow.createCell(0).setCellValue("Values");

        // Write each value in its own row
        for (Number value : values) {
            Row valueRow = sheet.createRow(rowIndex++);
            String valueString = String.valueOf(value != null ? value : "");

            // Check the length of the value and truncate if necessary
            if (valueString.length() > 32767) {
                valueString = valueString.substring(0, 32767) + "... (truncated)";

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
                List<String> ExcelHeaders = List.of("file name", "timestamp");
                Map<String, Object> xlsxResult = createExcelWithMultipleSheets(logicalData, type, ExcelHeaders);
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
