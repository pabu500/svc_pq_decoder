package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.CompressionAlgorithm;
import com.pabu5h.pq_decoder.physical_parser.CompressionStyle;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.util.GUID;
import org.springframework.stereotype.Component;

@Component
public class ContainerRecord {
	
    public static GUID TitleTag = new GUID("8973860d-f1c3-11cf-9d89-0080c72e70a3");
    //
    // Summary:
    //     Tag that identifies the subject applied to the PQDIF file.
    public static GUID SubjectTag = new GUID("8973860e-f1c3-11cf-9d89-0080c72e70a3");
    //
    // Summary:
    //     Tag that identifies the version info.
    public static GUID VersionInfoTag = new GUID("89738607-f1c3-11cf-9d89-0080c72e70a3");
    //
    // Summary:
    //     Tag that identifies the file name.
    public static GUID FileNameTag = new GUID("89738608-f1c3-11cf-9d89-0080c72e70a3");
    //
    // Summary:
    //     Tag that identifies the date and time of creation.
    public static GUID CreationTag = new GUID("89738609-f1c3-11cf-9d89-0080c72e70a3");
    //
    // Summary:
    //     Tag that identifies the notes stored in the PQDIF file.
    public static GUID NotesTag = new GUID("89738617-f1c3-11cf-9d89-0080c72e70a3");
    //
    // Summary:
    //     Tag that identifies the compression style of the PQDIF file.
    public static GUID CompressionStyleTag = new GUID("8973861b-f1c3-11cf-9d89-0080c72e70a3");
    //
    // Summary:
    //     Tag that identifies the compression algorithm used when writing the PQDIF file.
    public static GUID CompressionAlgorithmTag = new GUID("8973861c-f1c3-11cf-9d89-0080c72e70a3");
    
    Record record;
    
    public ContainerRecord(Record record) {
		this.record = record;
	}

	public static ContainerRecord createContainerRecord(Record record) {
		//if (physicalRecord.Header.TypeOfRecord != 0)
    	if (record.getHeader().getTypeOfRecord() != RecordType.Container) {
    		return null;
    	}
    	
    	return new ContainerRecord(record);
    }
	
	
    public CompressionAlgorithm getCompressionAlgorithm() {
        ScalarElement scalarByTag = record.getBody().getCollection().getScalarByTag(CompressionAlgorithmTag);
		
        if (scalarByTag != null) {
        	return CompressionAlgorithm.from(scalarByTag.getUInt4());
        }

        return null;
    }
    
    public CompressionStyle getCompressionStyle() {
        ScalarElement scalarByTag = record.getBody().getCollection().getScalarByTag(CompressionStyleTag);
		
        if (scalarByTag != null) {
        	return CompressionStyle.from(scalarByTag.getUInt4());
        }

        return null;
    }
}
