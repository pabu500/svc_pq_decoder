package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.*;
import com.pabu5h.pq_decoder.physical_parser.Record;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Component
@Setter
@Getter
public class LogicalParser {
    Logger logger = Logger.getLogger(LogicalParser.class.getName());


    private ArrayList DataSourceRecords;
    private String filePath;
    private PhysicalParser physicalParser;
    private DataSourceRecord currentDataSourceRecord;
    private ObservationRecord nextObservationRecord;
    private MonitorSettingRecord currentMonitorSettingsRecord;
    private List<DataSourceRecord> dataSourceRecords = new ArrayList<>();


    @Autowired
    private ContainerRecord containerRecord;

    public LogicalParser(@Value("${file.path}") String filePath) {
        this.filePath = filePath;
        this.physicalParser = new PhysicalParser(this.filePath);
        this.DataSourceRecords = new ArrayList<DataSourceRecord>();
    }

    @Async
    public CompletableFuture<Void> openAsync() {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalStateException("Unable to open PQDIF file when no file name has been defined.");
        }

        // Ensuring the file opens asynchronously and waits for completion before proceeding
        return CompletableFuture.runAsync(() -> {
            try {
                this.physicalParser.openAsync().get(); // Ensure the file is opened before proceeding
            } catch (Exception e) {
                throw new RuntimeException("Error opening PQDIF file", e);
            }
        }).thenCompose(ignored -> CompletableFuture.runAsync(() -> {
            try {
                this.readContainerRecordAsync(); // Process container record after file is open
            } catch (IOException e) {
                logger.severe("Error reading container record: " + e.getMessage());
            }
        }));
    }


    @Async
    public void readContainerRecordAsync() throws IOException {
        Record record = this.physicalParser.getNextRecord();
        if(record==null){
            logger.severe("No record found");
        }
        ContainerRecord containerRecord = ContainerRecord.createContainerRecord(record);
        if(containerRecord==null){
            logger.severe("The first record in a PQDIF file must be a container record.");
        }
        assert containerRecord != null;
        this.physicalParser.compressionAlgorithm = containerRecord.getCompressionAlgorithm();
        this.physicalParser.compressionStyle = containerRecord.getCompressionStyle();
//        this.physicalParser.compressionAlgorithm = CompressionAlgorithm.Zlib;
//        this.physicalParser.compressionStyle = CompressionStyle.RecordLevel;
        CompletableFuture.completedFuture(null);
    }

    public boolean hasNextObservationRecordAsync() {
        //Once found an ObservationRecord or physical parser has no more record
        //exits the loop and return true
        int count = 1;
        while (nextObservationRecord == null && this.physicalParser.hasNextRecord) {
            try {
                logger.info("current record: "+(count++));
                // Read subsequent records
                Record record = physicalParser.getNextRecord();
                switch (record.getHeader().getTypeOfRecord()) {
                    case RecordType.DataSource:
                        currentDataSourceRecord = DataSourceRecord.createDataSourceRecord(record);
                        if (currentDataSourceRecord == null) {
                            logger.severe("Invalid assumption: record type is data source, yet the data source record was not created.");
                            break;
                        }
                        dataSourceRecords.add(currentDataSourceRecord);
                        break;
                    case RecordType.MonitorSettings:
                        currentMonitorSettingsRecord = MonitorSettingRecord.createMonitorSettingRecord(record);
                        break;
                    case RecordType.Observation:
                        if (currentDataSourceRecord == null) {
                            logger.severe("Found observation record before finding data source record.");
                            break;
                        }
                        nextObservationRecord = ObservationRecord.createObservationRecord(record, currentDataSourceRecord, currentMonitorSettingsRecord);
                        break;
                    case RecordType.Container:
                        logger.severe("Found more than one container record in PQDIF file.");
                }

            } catch (Exception e) {
                logger.severe("Error processing record: "+ e);
                break;
            }
        }
        return nextObservationRecord != null;

    }

    public CompletableFuture<ObservationRecord> nextObservationRecordAsync() {
        return CompletableFuture.supplyAsync(() -> {
            this.hasNextObservationRecordAsync();  // Ensure the next observation record is available

            // Get the next observation record
            ObservationRecord nextObservationRecordNew = this.nextObservationRecord;
            this.nextObservationRecord = null;

            // If no record exists, throw an exception
            if (nextObservationRecordNew == null) {
                throw new IllegalStateException("There are no more observation records in the PQDIF file.");
            }

            return nextObservationRecordNew;  // Return the record
        });
    }



}
