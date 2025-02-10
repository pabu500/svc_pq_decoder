package com.pabu5h.pq_decoder.controller;

import com.pabu5h.pq_decoder.util.ExcelUtil;
import com.pabu5h.pq_decoder.physical_parser.EndOfStreamException;
import com.pabu5h.pq_decoder.physical_parser.PhysicalParser;
//import com.pabu5h.comtrade.physicalParser.Record;
import com.pabu5h.pq_decoder.processor.PhysicalParserProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;


@Slf4j
@RestController
public class PQDController {
    Logger logger = Logger.getLogger(PQDController.class.getName());

    @Value("${file.path}")
    private String filePath;
    @Value("${pqd.exe.path}")
    private String pqdExePath;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PhysicalParserProcessor physicalParserProcessor;
    @Autowired
    private ExcelUtil excelUtil;

    @Autowired
    private com.pabu5h.pq_decoder.physical_parser.Record record;


    @PostMapping("/process_pqd_file")
    public ResponseEntity<Object> parsePQDFile(@RequestParam("pqd_file") MultipartFile pqdFile,
                                                           @RequestParam("operation") String operation,
                                                           @RequestParam(value = "sample_step", required = false, defaultValue = "1") String samplingStep,
                                                           @RequestParam(value = "filename", required = false, defaultValue = "data1") String filename) throws IOException {
        int step;
        Map<String,Object> errorMap = new HashMap<>();
        try{
            step = Integer.parseInt(samplingStep);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", "Invalid sampling step"));
        }

        if(pqdFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", "PQD file is empty"));
        }

        String url = pqdExePath;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // Step 1: Get InputStream from MultipartFile
        InputStream inputStream = pqdFile.getInputStream();

        // Step 2: Convert InputStream to byte array (if needed)
        byte[] fileBytes = inputStream.readAllBytes();

        // Step 3: Create a ByteArrayResource for multiple reads
        ByteArrayResource byteArrayResource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return pqdFile.getOriginalFilename();  // Optional: Preserve the original file name
            }
        };

        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("file", byteArrayResource);  // Add file to request
        request.add("step", step);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Map<String, Object>> resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> result = resp.getBody();
        if(result == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Failed to process PQD file"));
        }
        if(result.get("data") == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Failed to process PQD file"));
        }

        Map<String,Object> data = (Map<String, Object>) result.get("data");
        Map<String,Object> pqdData = (Map<String, Object>) data.get("pqd_data");
        Map<String,Object> logicalData = (Map<String, Object>) pqdData.get("logical_parser");
        Map<String,Object> physicalData = (Map<String, Object>) pqdData.get("physical_parser");
        if(physicalData == null){
            errorMap.put("physical_parser_error", "Failed to parse physical data");
        }
        if(logicalData == null){
            errorMap.put("logical_parser_error", "Failed to parse logical data");
        }

        if(!Objects.equals(operation, "plotGraph")) {
                Map<String,Object> mapResp = excelUtil.convertToZipFile(operation, filename, data, logicalData,"pqd");
                byte[] zipBytes = (byte[]) mapResp.get("result");
                String uniqueFilename = filename + "_" + System.currentTimeMillis();
                HttpHeaders respHeaders = new HttpHeaders();
                respHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                respHeaders.add("Content-Disposition", "attachment; filename=\"" + uniqueFilename + ".zip\"");
                logger.info(filename + ".zip");
                respHeaders.add("Content-Type", "application/zip");  // Correct Content-Type for ZIP files
                respHeaders.add("Content-Length", String.valueOf(zipBytes.length));
                return ResponseEntity.status(HttpStatus.OK).headers(respHeaders).body(zipBytes);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("error", errorMap);
        response.put("data", data);
        return ResponseEntity.ok().body(
                response
        );
    }

    @PostMapping("/process_physical_parser")
    public ResponseEntity<Map<String,Object>> physicalParser() throws IOException, EndOfStreamException, ExecutionException, InterruptedException {
        PhysicalParser physicalParser = new PhysicalParser(filePath);
        long nextPosition = 0;
        physicalParser.openAsync().get(); // This will block until the file is opened
        if (physicalParser.hasNextRecord) {  // Initial check
            log.info("Reading first record...");
            record = physicalParser.getNextRecord(nextPosition);

            // Get next record position
           nextPosition = record.getHeader().getNextRecordPosition();

            // Read subsequent records
            while (nextPosition > 0) {  // If nextPosition is 0, we stop
                log.info("Reading next record at position: " + nextPosition);
                record = physicalParser.getNextRecord(nextPosition);
                nextPosition = record.getHeader().getNextRecordPosition(); // Update next position
            }
        }
//        while(physicalParser.hasNextRecord){
//            log.info("next record found");
//            physicalParser.getNextRecord();
////            physicalParser.getNextRecord();
////            Record record = physicalParser.getNextRecord();
//        }
        return ResponseEntity.ok().body(Map.of("success", true, "data", "aa"));
    }

//    @PostMapping("/process_logical_parser")
//    public ResponseEntity<Map<String,Object>> logicalParser() throws IOException, EndOfStreamException, ExecutionException, InterruptedException {
//        PhysicalParser physicalParser = new PhysicalParser(filePath);
//        physicalParser.openAsync().get(); // This will block until the file is opened
//
//        while(physicalParser.hasNextRecord){
//            log.info("next record found");
//            physicalParser.getNextRecord();
////            physicalParser.getNextRecord();
////            Record record = physicalParser.getNextRecord();
//        }
//        return ResponseEntity.ok().body(Map.of("success", true, "data", "aa"));
//    }
}
