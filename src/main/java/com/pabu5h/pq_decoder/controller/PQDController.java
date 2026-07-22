package com.pabu5h.pq_decoder.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.pabu5h.pq_decoder.PqdModule;
import com.pabu5h.pq_decoder.util.ExcelUtil;

import jakarta.annotation.PostConstruct;

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
    private ExcelUtil excelUtil;
    @Autowired
    private PqdModule pqdModule;

    @PostMapping("/process_pqd_file")
    public ResponseEntity<Object> parsePQDFile(@RequestParam("pqd_file") MultipartFile pqdFile,
                                               @RequestParam("response_type") String responseType,
                                               @RequestParam(value = "sample_step", required = false, defaultValue = "1") String samplingStep,
                                               @RequestParam(value = "filename", required = false, defaultValue = "pqd_result") String filename) throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String,Object> errorMap = new HashMap<>();
        try{
            Integer.parseInt(samplingStep);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", "Invalid sampling step"));
        }

        if(pqdFile.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", "PQD file is empty"));
        }

        String filepath = saveTempFile(pqdFile);

        Map<String,Object> result = pqdModule.extractLogicalData(filepath,samplingStep);
        if(result == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Failed to process PQD file"));
        }
        if(result.get("data") == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Failed to process PQD file"));
        }

        Map<String,Object> data = (Map<String, Object>) result.get("data");
        Map<String,Object> pqdData = (Map<String, Object>) data.get("pqd_data");
        ArrayList<Map<String,Object>>  logicalData = (ArrayList<Map<String, Object>>) pqdData.get("logical_parser");
//        ArrayList<Map<String,Object>>  physicalData = (ArrayList<Map<String, Object>>) pqdData.get("physical_parser");
//        if(physicalData == null){
//            errorMap.put("physical_parser_error", "Failed to parse physical data");
//        }
        if(logicalData == null){
            errorMap.put("logical_parser_error", "Failed to parse logical data");
        }
        assert logicalData != null;
        Map<String,Object> logicalDataMap = logicalData.isEmpty() ? null : (Map<String, Object>) logicalData.get(0);

        switch (responseType) {
            case "data":
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("success", true);
                response.put("error", errorMap);
                response.put("data", data);
                response.put("processing_time_seconds", (System.currentTimeMillis() - startTime) / 1000.0);
                return ResponseEntity.ok().body(
                        response
                );
            case "xlsx":
            case "json":
                Map<String,Object> mapResp = excelUtil.convertToZipFile(responseType, filename, data, logicalDataMap,"pqd");
                byte[] zipBytes = (byte[]) mapResp.get("result");
                String uniqueFilename = filename + "_" + System.currentTimeMillis();
                HttpHeaders respHeaders = new HttpHeaders();
                respHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                respHeaders.add("Content-Disposition", "attachment; filename=\"" + uniqueFilename + ".zip\"");
                logger.info(filename + ".zip");
                respHeaders.add("Content-Type", "application/zip");  // Correct Content-Type for ZIP files
                respHeaders.add("Content-Length", String.valueOf(zipBytes.length));
                respHeaders.add("X-Processing-Time-Seconds", String.valueOf((System.currentTimeMillis() - startTime) / 1000.0));
                return ResponseEntity.status(HttpStatus.OK).headers(respHeaders).body(zipBytes);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "error", "Invalid responseType"));
        }
    }

    // Method to save the file temporarily
    public String saveTempFile(MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get("uploads/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Check if there are any files in the directory
        try (Stream<Path> files = Files.list(uploadPath)) {
            if (files.findAny().isPresent()) { // If there are files in the directory
                // Delete all files in the directory
                Files.walk(uploadPath)
                        .filter(Files::isRegularFile) // Only delete regular files
                        .forEach(path -> {
                            try {
                                Files.delete(path); // Delete each file
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to delete file: " + path, e);
                            }
                        });
            }
        }

        // Generate a unique filename if needed
        String filename = multipartFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        // Save the file
        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }


//    @GetMapping("/process_physical_parser")
//    public ResponseEntity<Map<String,Object>> physicalParser() throws IOException, EndOfStreamException, ExecutionException, InterruptedException {
//        PhysicalParser physicalParser = new PhysicalParser(filePath);
//
//        physicalParser.openAsync().get(); // This will block until the file is opened
//        // Initial check
//        int count = 0;
//        List<Record> recordsList = new ArrayList<>();
//        while (physicalParser.hasNextRecord) {
//        	if (count == 0) {
//        		log.info("Reading first record...");
//        	} else {
//        		// Get next record position
//                long nextPosition = physicalParser.currentStreamPosition;
//                log.info("Reading next record at position: " + nextPosition);
//        	}
//            // Read subsequent records
//			Record record = physicalParser.getNextRecord();
//			ContainerRecord containerRecord = ContainerRecord.createContainerRecord(record);
//			if (containerRecord != null) {
//				physicalParser.compressionAlgorithm = containerRecord.getCompressionAlgorithm();
//				physicalParser.compressionStyle = containerRecord.getCompressionStyle();
//			}
//
//			log.info("Record " + (count++) + ": --> " + record);
//			recordsList.add(record);
//        }
//
//        return ResponseEntity.ok().body(Map.of("success", true, "data", recordsList));
//    }
//
//    @GetMapping("/process_logical_parser")
//    public ResponseEntity<Map<String,Object>> logicalParser() throws IOException, EndOfStreamException, ExecutionException, InterruptedException {
//        Map<String, Object> logicalData = new LinkedHashMap<>();
//        try {
//            String filePath = "C:\\Users\\yaoyj\\Desktop\\pq_decoder_henry\\svc_pq_decoder_4_april\\src\\main\\resources\\TAMPINES NT 22kV DE_20230208001841.pqd";
//            LogicalParser logicalParser = new LogicalParser(filePath);
//            logicalParser.open();
//
//            List<ObservationRecord> recordsList = new ArrayList<>();
//
//            while (logicalParser.hasNextObservationRecord()) {
//                ObservationRecord record = logicalParser.nextObservationRecord();
//                System.out.println(new ObjectMapper().writeValueAsString(record));
//                recordsList.add(record);
//            }
//
//            logicalData.put("logical_parser", recordsList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok().body(Map.of("success", true, "data", logicalData));
//    }


    @GetMapping("/process_pqd_file_test")
    public ResponseEntity<Object> parsePQDFileTest(
                                               @RequestParam(value = "response_type", required = false, defaultValue = "data") String responseType,
                                               @RequestParam(value = "sample_step", required = false, defaultValue = "1") String samplingStep,
                                               @RequestParam(value = "filename", required = false, defaultValue = "pqd_result") String filename) throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String,Object> errorMap = new HashMap<>();
        try{
            Integer.parseInt(samplingStep);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"error", "Invalid sampling step"));
        }
        
        //SATEC
        //String filepath = "E:\\workspace\\PA\\ext\\EventsTrends_SATEC\\Trends\\BANYAN DR 22kV CD_26-07-09 15-40.pqd";
        //String filepath = "E:\\workspace\\PA\\ext\\EventsTrends_SATEC\\Events\\CRAWFORD 22kV AB_26-06-16 14-03-19-381.pqd";
        
        //DRANETZ
        //String filepath = "E:\\workspace\\PA\\ext\\Sampledata\\event\\temp10.pqd";
        String filepath = "E:\\workspace\\PA\\ext\\Sampledata\\trend\\temp8.pqd";

        Map<String,Object> result = pqdModule.extractLogicalData(filepath,samplingStep);
        if(result == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Failed to process PQD file"));
        }
        if(result.get("data") == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Failed to process PQD file"));
        }

        Map<String,Object> data = (Map<String, Object>) result.get("data");
        Map<String,Object> pqdData = (Map<String, Object>) data.get("pqd_data");
        ArrayList<Map<String,Object>>  logicalData = (ArrayList<Map<String, Object>>) pqdData.get("logical_parser");
//        ArrayList<Map<String,Object>>  physicalData = (ArrayList<Map<String, Object>>) pqdData.get("physical_parser");
//        if(physicalData == null){
//            errorMap.put("physical_parser_error", "Failed to parse physical data");
//        }
        if(logicalData == null){
            errorMap.put("logical_parser_error", "Failed to parse logical data");
        }
        assert logicalData != null;
        Map<String,Object> logicalDataMap = logicalData.isEmpty() ? null : (Map<String, Object>) logicalData.get(0);

        switch (responseType) {
            case "data":
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("success", true);
                response.put("error", errorMap);
                response.put("data", data);
                response.put("processing_time_seconds", (System.currentTimeMillis() - startTime) / 1000.0);
                return ResponseEntity.ok().body(
                        response
                );
            case "xlsx":
            case "json":
                Map<String,Object> mapResp = excelUtil.convertToZipFile(responseType, filename, data, logicalDataMap,"pqd");
                byte[] zipBytes = (byte[]) mapResp.get("result");
                String uniqueFilename = filename + "_" + System.currentTimeMillis();
                HttpHeaders respHeaders = new HttpHeaders();
                respHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                respHeaders.add("Content-Disposition", "attachment; filename=\"" + uniqueFilename + ".zip\"");
                logger.info(filename + ".zip");
                respHeaders.add("Content-Type", "application/zip");  // Correct Content-Type for ZIP files
                respHeaders.add("Content-Length", String.valueOf(zipBytes.length));
                respHeaders.add("X-Processing-Time-Seconds", String.valueOf((System.currentTimeMillis() - startTime) / 1000.0));
                return ResponseEntity.status(HttpStatus.OK).headers(respHeaders).body(zipBytes);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "error", "Invalid responseType"));
        }
    }
    
//    @PostConstruct
    public void test() {
        try {
//             Map<String,Object> result = pqdModule.extractLogicalData("D:\\workspace\\ext\\EventsTrends_SATEC\\Trends\\BANYAN DR 22kV CD_26-07-09 15-40.pqd","1");
//             System.out.println(result);((Map<String,Object>) ((Map<String,Object>) (result.get("data"))).get("pqd_data")).get("logical_parser");
        } catch (Exception e) {

        	e.printStackTrace();
        }

    }
}
