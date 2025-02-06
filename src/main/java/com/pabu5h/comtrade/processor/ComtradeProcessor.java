package com.pabu5h.comtrade.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public Map<String, Object> processPqd(InputStream cfgInputStream, InputStream datInputStream, int step, String operation,String filename) throws IOException {
        Map<String, Object> result = comtradeModule.processCfgConfig(cfgInputStream, datInputStream, step);
        ComtradeConfig comtradeResult = (ComtradeConfig) result.get("result");
        Map<String, Object> convertedConfigMap = convertConfigFieldsToMap(comtradeResult);
        Map<String, Object> comtradeMap = (Map<String, Object>) convertedConfigMap.get("result");
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
}
