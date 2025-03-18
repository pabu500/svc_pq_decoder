package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.PhysicalParser;
import com.pabu5h.pq_decoder.physical_parser.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Component
public class LogicalParser {
    Logger logger = Logger.getLogger(LogicalParser.class.getName());


//    private ArrayList DataSourceRecords;
//    private String filePath;
//    private PhysicalParser physicalParser;
//    private DataSourceRecord currentDataSourceRecord;
//    private ObservationRecord nextObservationRecord;
//    private List<DataSourceRecord> dataSourceRecords = new ArrayList<>();
//
//    public LogicalParser(String filePath) {
//
//        logger.info("LogicalParser created with PhysicalParser.");
//        logger.info("PhysicalParser created with file path: " + filePath);
//        this.filePath = filePath;
//        this.physicalParser = new PhysicalParser(this.filePath);
//        this.DataSourceRecords = new ArrayList<DataSourceRecord>();
//    }
//
//    public CompletableFuture<Void> openAsync() {
//        if (filePath == null || filePath.isEmpty()) {
//            throw new IllegalStateException("Unable to open PQDIF file when no file name has been defined.");
//        }
//
//        CompletableFuture.runAsync(() -> {
//           this.physicalParser.openAsync();
//        });
//
//        return CompletableFuture.runAsync(()->{
//
//        });
//    }

//    private void ReadContainerRecordAync(){
//        CompletableFuture.runAsync(()->{
//            try {
//                Record record = this.physicalParser.getNextRecordAsync().get();
//                ContainerRecord containerRecord = ContainerRecord.createContainerRecord(record);
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        });
//    }

//    public CompletableFuture<Boolean> hasNextObservationRecordAsync() {
//        return CompletableFuture.supplyAsync(() -> {
//            while (nextObservationRecord == null && physicalParser.hasNextRecord()) {
//                try {
//                    Record record = physicalParser.getNextRecordAsync().join(); // Blocking for async result
//
//                    switch (record.getHeader().getTypeOfRecord()) {
//                        case DATA_SOURCE:
//                            currentDataSourceRecord = DataSourceRecord.createDataSourceRecord(record);
//                            if (currentDataSourceRecord == null) {
//                                throw new IllegalArgumentException("Invalid assumption: record type is data source, yet the data source record was not created.");
//                            }
//                            dataSourceRecords.add(currentDataSourceRecord);
//                            break;
//
//                        case OBSERVATION:
//                            if (currentDataSourceRecord == null) {
//                                throw new IllegalArgumentException("Found observation record before finding data source record.");
//                            }
//                            nextObservationRecord = ObservationRecord.createObservationRecord(record, currentDataSourceRecord, currentMonitorSettingsRecord);
//                            break;
//                        case CONTAINER:
//                            throw new IllegalStateException("Found more than one container record in PQDIF file.");
//                    }
//                } catch (Exception e) {
//                    throw new RuntimeException("Error processing record", e);
//                }
//            }
//            return nextObservationRecord != null;
//        });
//    }



}
