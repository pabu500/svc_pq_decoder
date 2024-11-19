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
public class ComtradeController {
    Logger logger = Logger.getLogger(ComtradeController.class.getName());
    @Autowired
    private ComtradeProcessor comtradeProcessor;

    @PostMapping("process_pqd")
    public ResponseEntity<Object> processPqd(@RequestParam("cfgFile") MultipartFile cfgFile,
                                                         @RequestParam("datFile") MultipartFile datFile,
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

        Map<String, Object> comtradeConfig = comtradeProcessor.processPqd(
                cfgFile.getInputStream(), datFile.getInputStream(),step,operation,filename);
        Object result = comtradeConfig.get("result") != null ? comtradeConfig.get("result") : new ArrayList<>();
        Object error = comtradeConfig.get("error") != null ? comtradeConfig.get("error") : new ArrayList<>();
        Map<String, Object> response = new LinkedHashMap<>();
        if ("downloadCsvZip".equals(operation) || "downloadJsonZip".equals(operation)) {
            if("downloadCsvZip".equals(operation)) {
                logger.info("sending csv zip file to client");
            }else{
                logger.info("sending json zip file to client");
            }
            byte[] zipBytes;
            try{
                zipBytes  = (byte[]) result;
            }catch(Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success",false,"error", "cannot convert to byte"));
            }

            // Set the headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + filename + ".zip");
            switch(operation){
                case "downloadCsvZip":
                    headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    break;
                case "downloadJsonZip":
                    headers.add("Content-Type", "application/json");
                    break;
                default:
                    logger.info("Invalid operation");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("success", false, "error", "Invalid operation: " + operation));
            }
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
