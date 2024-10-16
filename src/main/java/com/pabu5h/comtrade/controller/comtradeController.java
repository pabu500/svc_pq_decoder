package com.pabu5h.comtrade.controller;

import com.pabu5h.comtrade.processor.ComtradeProcessor;
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
public class comtradeController {
    Logger logger = Logger.getLogger(comtradeController.class.getName());
    @Autowired
    private ComtradeProcessor comtradeProcessor;

    @PostMapping("process_pqd")
    public ResponseEntity<Object> processPqd(@RequestParam("cfgFile") MultipartFile cfgFile,
                                                         @RequestParam("datFile") MultipartFile datFile,
                                                         @RequestParam("operation") String operation,
                                                         @RequestParam(value = "sample_step", required = false, defaultValue = "1") String samplingStep) throws IOException {
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

        Map<String, Object> comtradeConfig = comtradeProcessor.processPqd(
                cfgFile.getInputStream(), datFile.getInputStream(),step,operation);
        Object result = comtradeConfig.get("result") != null ? comtradeConfig.get("result") : new ArrayList<>();
        Object error = comtradeConfig.get("error") != null ? comtradeConfig.get("error") : new ArrayList<>();
        Map<String, Object> response = new LinkedHashMap<>();
        if ("downloadCsvZip".equals(operation)) {
            logger.info("sending csv zip file to client");

            byte[] zipBytes = (byte[]) result;
            logger.info("result: " + result);
            // Set the headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=data.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(zipBytes);
        }
        response.put("success", true);
        response.put("error", error);
        response.put("result", result);
        return ResponseEntity.ok().body(
                response
        );
    }


}
