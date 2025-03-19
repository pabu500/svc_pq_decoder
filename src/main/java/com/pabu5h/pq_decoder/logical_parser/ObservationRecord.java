package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ObservationRecord {
    public Record physicalRecord;
    public DataSourceRecord dataSource;
    public MonitorSettingRecord settings;
    private String observationId;
    private String observationValue;

    private ObservationRecord(Record physicalRecord, DataSourceRecord dataSource, MonitorSettingRecord settings){
        this.physicalRecord = physicalRecord;
        this.dataSource = dataSource;
        this.settings = settings;
    }

//    public String getName() {
//        VectorElement
//    }
    public static ObservationRecord createObservationRecord(Record physicalRecord, DataSourceRecord dataSource, MonitorSettingRecord settings){
        if(physicalRecord.getHeader().getTypeOfRecord()!= RecordType.Observation){
            return null;
        }
        return new ObservationRecord(physicalRecord,dataSource,settings);
    }
    // Add fields, constructors, getters, setters, and toString
}
