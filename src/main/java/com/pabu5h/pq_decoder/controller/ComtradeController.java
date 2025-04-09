package com.pabu5h.pq_decoder.controller;

import com.pabu5h.pq_decoder.util.ExcelUtil;
import com.pabu5h.pq_decoder.processor.ComtradeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@RestController
public class ComtradeController {
    Logger logger = Logger.getLogger(ComtradeController.class.getName());
    @Autowired
    private ComtradeProcessor comtradeProcessor;
    @Autowired
    private ExcelUtil excelUtil;

    @PostMapping("/process_comtrade_file")
    public ResponseEntity<Object> processPqd(@RequestParam("cfg_file") MultipartFile cfgFile,
                                                         @RequestParam("dat_file") MultipartFile datFile,
                                                         @RequestParam("operation") String operation,
                                                         @RequestParam(value = "sample_step", required = false, defaultValue = "1") String samplingStep,
                                                         @RequestParam(value = "filename", required = false, defaultValue = "data1") String filename) throws IOException {
        int step;
        try{
            step = Integer.parseInt(samplingStep);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", "Invalid sampling step"));
        }

        Map<String,Object> validatedCfgResult = comtradeProcessor.validateFile(cfgFile);
        Map<String,Object> validatedDatResult = comtradeProcessor.validateFile(datFile);
        if(validatedCfgResult.get("error") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", validatedCfgResult.get("error")));
        }
        if(validatedDatResult.get("error") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", validatedDatResult.get("error")));
        }

        Map<String, Object> comtradeConfig = comtradeProcessor.processComtradeFile(
                cfgFile.getInputStream(), datFile.getInputStream(),step,operation,filename);
        Object result = comtradeConfig.get("result") != null ? comtradeConfig.get("result") : new ArrayList<>();
        Object error = comtradeConfig.get("error") != null ? comtradeConfig.get("error") : new ArrayList<>();
        Map<String, Object> response = new LinkedHashMap<>();
//        if ("downloadCsvZip".equals(operation) || "downloadJsonZip".equals(operation)) {
//            if("downloadCsvZip".equals(operation)) {
//                logger.info("sending csv zip file to client");
//            }else{
//                logger.info("sending json zip file to client");
//            }
//            byte[] zipBytes;
//            try{
//                zipBytes  = (byte[]) result;
//            }catch(Exception e){
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success",false,"error", "cannot convert to byte"));
//            }
//
//            // Set the headers for the response
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment; filename=" + filename + ".zip");
//            headers.add("Content-Type", "application/zip");
//            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(zipBytes);
//        }
        Map<String,Object> data = (Map<String, Object>) result;
        if(!Objects.equals(operation, "plotGraph")) {
            Map<String,Object> mapResp = excelUtil.convertToZipFile(operation, filename, data, data,"comtrade");
            byte[] zipBytes = (byte[]) mapResp.get("result");
            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            respHeaders.add("Content-Disposition", "attachment; filename=\"" + filename + ".zip\"");
            logger.info(filename + ".zip");
            respHeaders.add("Content-Type", "application/zip");  // Correct Content-Type for ZIP files
            return ResponseEntity.status(HttpStatus.OK).headers(respHeaders).body(zipBytes);
        }

        response.put("success", true);
        response.put("error", error);
        response.put("result", result);
        return ResponseEntity.ok().body(
                response
        );
    }


}
