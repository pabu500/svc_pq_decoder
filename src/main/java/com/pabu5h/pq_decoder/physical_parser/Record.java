package com.pabu5h.pq_decoder.physical_parser;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pabu5h.pq_decoder.util.GUID;

import lombok.Getter;
import org.springframework.context.annotation.Bean;

@Getter
public class Record {
	
	public static final Map<GUID, RecordType> RecordTypeTagMap = createRecordTypeTagMap();
	
    private final RecordHeader header;
    private final RecordBody body;
//    private final RecordType recordType;  // NEW: Store the type of record

    public Record(RecordHeader header, RecordBody body) {
        this.header = header;
        this.body = body;
//        this.recordType = determineRecordType(header);
    }
    
    public Record(GUID recordTypeTag) {
		this.header = new RecordHeader();
		this.body = new RecordBody();
		// TODO Auto-generated constructor stub
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
    
    public RecordBody getBody() {
    	return body;
    }

    private RecordType determineRecordType(RecordHeader header) {
        return RecordTypeTagMap.getOrDefault(header.getRecordTypeTag(), RecordType.Unknown);
    }

	public static GUID getTypeAsTag(RecordType datasource) {
		// TODO Auto-generated method stub
		return null;
	}
    
    

//    public RecordType getRecordType() {
//        return this.recordType;
//    }
}

