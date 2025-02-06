package com.pabu5h.comtrade.Interface;

import com.pabu5h.comtrade.logicalParser.ContainerRecord;
import com.pabu5h.comtrade.logicalParser.DataSourceRecord;
import com.pabu5h.comtrade.logicalParser.ObservationRecord;

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
