package com.pabu5h.comtrade.controller;

import com.pabu5h.comtrade.processor.ComtradeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class comtradeController {

    @Autowired
    private ComtradeProcessor comtradeProcessor;

    @PostMapping("process_pqd")
    public ResponseEntity<Map<String,Object>> processPqd(@RequestParam("cfgFile") MultipartFile cfgFile,
                                                         @RequestParam("datFile") MultipartFile datFile) throws IOException {
        Map<String,Object> validatedCfgResult = comtradeProcessor.validateFile(cfgFile);
        Map<String,Object> validatedDatResult = comtradeProcessor.validateFile(datFile);
        if(validatedCfgResult.get("error") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", validatedCfgResult.get("error")));
        }
        if(validatedDatResult.get("error") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", validatedDatResult.get("error")));
        }

        Map<String, Object> comtradeConfig = comtradeProcessor.processPqd(
                cfgFile.getInputStream(), datFile.getInputStream());
        Object result = comtradeConfig.get("result") != null ? comtradeConfig.get("result") : new ArrayList<>();
        Object error = comtradeConfig.get("error") != null ? comtradeConfig.get("error") : new ArrayList<>();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("error", error);
        response.put("result", result);
        return ResponseEntity.ok().body(
                response
        );
    }


}
