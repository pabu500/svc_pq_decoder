package com.pabu5h.pq_decoder.Interface;

import com.pabu5h.pq_decoder.logical_parser.ContainerRecord;
import com.pabu5h.pq_decoder.logical_parser.DataSourceRecord;
import com.pabu5h.pq_decoder.logical_parser.ObservationRecord;

import java.util.List;

public interface LogicalParser {
    void open(String filePath) throws Exception;

    ContainerRecord getContainerRecord() throws Exception;

    String getFilePath();

    List<DataSourceRecord> getDataSourceRecords() throws Exception;

    boolean hasNextObservationRecord();

    ObservationRecord getNextObservationRecord() throws Exception;

    void close() throws Exception;
}
