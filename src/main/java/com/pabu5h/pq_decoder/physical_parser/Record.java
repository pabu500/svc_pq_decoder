package com.pabu5h.pq_decoder.physical_parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pabu5h.pq_decoder.util.GUID;

import lombok.Getter;

@Getter
public class Record {
	
	public static final Map<GUID, RecordType> RecordTypeTagMap = createRecordTypeTagMap();
	
    private final RecordHeader header;
    private final RecordBody body;

    public Record(RecordHeader header, RecordBody body) {
        this.header = header;
        this.body = body;
    }
    
    private static Map<GUID, RecordType> createRecordTypeTagMap() {
    	
    	Map<GUID, RecordType> rs = new LinkedHashMap<>();
    	rs.put(new GUID("89738606-f1c3-11cf-9d89-0080c72e70a3"), RecordType.Container);
    	rs.put(new GUID("89738619-f1c3-11cf-9d89-0080c72e70a3"), RecordType.DataSource);
    	rs.put(new GUID("b48d858c-f5f5-11cf-9d89-0080c72e70a3"), RecordType.MonitorSettings);
    	rs.put(new GUID("8973861a-f1c3-11cf-9d89-0080c72e70a3"), RecordType.Observation);
    	rs.put(new GUID("89738618-f1c3-11cf-9d89-0080c72e70a3"), RecordType.Blank);
        
    	return rs;
    }
}

