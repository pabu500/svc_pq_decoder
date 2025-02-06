package com.pabu5h.comtrade.logicalParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class LogicalParser {

    private DataSourceRecord dataSourceRecord;
    private ArrayList observationRecord;

    @Autowired
    public LogicalParser() {
        this.dataSourceRecord = new DataSourceRecord();
        this.observationRecord = new ArrayList<DataSourceRecord>();
    }
}
