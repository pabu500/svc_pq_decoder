package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalParser;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
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
    public void openAsync() {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalStateException("Unable to open PQDIF file when no file name has been defined.");
        }

        CompletableFuture.runAsync(() -> {
           this.physicalParser.openAsync();
        });

        CompletableFuture.runAsync(() -> {
            try {
                this.readContainerRecordAsync();
            } catch (IOException e) {
                logger.severe("Error reading container record");
            }
        });
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
        CompletableFuture.completedFuture(null);
    }

    public boolean hasNextObservationRecordAsync() {
        while (nextObservationRecord == null && this.physicalParser.hasNextRecord) {
            int count = 0;
            try {
                logger.info("Reading first record...");

                // Read subsequent records
                Record record = physicalParser.getNextRecord();
                switch (record.getHeader().getTypeOfRecord()) {
                    case RecordType.DataSource:
                        currentDataSourceRecord = DataSourceRecord.createDataSourceRecord(record);
                        if (currentDataSourceRecord == null) {
                            logger.severe("Invalid assumption: record type is data source, yet the data source record was not created.");
                        }
                        dataSourceRecords.add(currentDataSourceRecord);
                        break;
                    case RecordType.MonitorSettings:
                        if (currentDataSourceRecord == null) {
                            logger.severe("Found monitor settings record before finding data source record.");
                        }
                            currentMonitorSettingsRecord = MonitorSettingRecord.createMonitorSettingRecord(record);
                    case RecordType.Observation:
                        if (currentDataSourceRecord == null) {
                            logger.severe("Found observation record before finding data source record.");
                        }
                        nextObservationRecord = ObservationRecord.createObservationRecord(record, currentDataSourceRecord, currentMonitorSettingsRecord);
                        break;
                    case RecordType.Container:
                        logger.severe("Found more than one container record in PQDIF file.");
                }
                logger.info("Record " + (count++) + ": --> " + record);
            } catch (Exception e) {
                logger.severe("Error processing record: "+ e);
            }
        }
        return nextObservationRecord != null;

    }

    public ObservationRecord nextObservationRecordAsync(){
        this.hasNextObservationRecordAsync();
        ObservationRecord nextObservationRecord = this.nextObservationRecord;
        this.setNextObservationRecord(null);
        if(nextObservationRecord == null){
            throw new IllegalStateException("There are no more observation records in the PQDIF file.");
        }
        return nextObservationRecord;
    }



}
