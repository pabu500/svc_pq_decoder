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
import java.util.Map;

@RestController
public class comtradeController {

    @Autowired
    private ComtradeProcessor comtradeProcessor;

    @PostMapping("process_pqd")
    public ResponseEntity<Map<String,Object>> processPqd(@RequestParam("cfgFile") MultipartFile cfgFile,
                                                         @RequestParam("datFile") MultipartFile datFile) {
        try {
            Map<String, Object> comtradeConfig = comtradeProcessor.processPqd(
                    cfgFile.getInputStream(), datFile.getInputStream());
            if(comtradeConfig.get("result")!=null){
                return ResponseEntity.ok(comtradeConfig);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", comtradeConfig.get("error"))); // 500 Internal Server Error
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage())); // 500 Internal Server Error
        }
    }
}
