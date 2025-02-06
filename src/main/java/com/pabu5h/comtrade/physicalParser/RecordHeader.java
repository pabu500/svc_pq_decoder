package com.pabu5h.comtrade.physicalParser;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RecordHeader {
    private long position;
    private UUID recordSignature;
    private UUID recordTypeTag;
    private int headerSize;
    private int bodySize;
    private int nextRecordPosition;
    private long checksum; // Unsigned int
    private byte[] reserved;

    // Getters and Setters
    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public UUID getRecordSignature() {
        return recordSignature;
    }

    public void setRecordSignature(UUID recordSignature) {
        this.recordSignature = recordSignature;
    }

    public UUID getRecordTypeTag() {
        return recordTypeTag;
    }

    public void setRecordTypeTag(UUID recordTypeTag) {
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
}
