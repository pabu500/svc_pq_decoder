package com.pabu5h.pq_decoder.physical_parser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pabu5h.pq_decoder.util.GUID;
import com.pabu5h.pq_decoder.util.GUID.GUIDSerializer;

public class RecordHeader {
    private long position;
    
    @JsonSerialize(using = GUIDSerializer.class)
    private GUID recordSignature;
    
    @JsonSerialize(using = GUIDSerializer.class)
    private GUID recordTypeTag;
    
    private int headerSize;
    private int bodySize;
    private int nextRecordPosition;
    private long checksum; // Unsigned int
    @JsonIgnore
    private byte[] reserved;

    // Getters and Setters
    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    @JsonSerialize(using = GUIDSerializer.class)
    public GUID getRecordSignature() {
        return recordSignature;
    }

    public void setRecordSignature(GUID recordSignature) {
        this.recordSignature = recordSignature;
    }

    @JsonSerialize(using = GUIDSerializer.class)
    public GUID getRecordTypeTag() {
        return recordTypeTag;
    }

    public void setRecordTypeTag(GUID recordTypeTag) {
        this.recordTypeTag = recordTypeTag;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public int getBodySize() {
        return bodySize;
    }

    public void setBodySize(int bodySize) {
        this.bodySize = bodySize;
    }

    public long getNextRecordPosition() {
        return nextRecordPosition;
    }

    public void setNextRecordPosition(int nextRecordPosition) {
        this.nextRecordPosition = nextRecordPosition;
    }

    public long getChecksum() {
        return checksum;
    }

    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }

    public byte[] getReserved() {
        return reserved;
    }

    public void setReserved(byte[] reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "RecordHeader{" +
                "position=" + position +
                ", recordSignature=" + recordSignature +
                ", recordTypeTag=" + recordTypeTag +
                ", headerSize=" + headerSize +
                ", bodySize=" + bodySize +
                ", nextRecordPosition=" + nextRecordPosition +
                ", checksum=" + checksum +
                '}';
    }

	public RecordType getTypeOfRecord() {
		RecordType rs = Record.RecordTypeTagMap.get(recordTypeTag);
		if (rs == null) {
			rs = RecordType.Unknown;
		}
		return rs;
	}
}
