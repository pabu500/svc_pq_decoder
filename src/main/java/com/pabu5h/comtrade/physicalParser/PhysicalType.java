package com.pabu5h.comtrade.physicalParser;

public enum PhysicalType {
    BOOLEAN1(1), BOOLEAN2(2), BOOLEAN4(4), CHAR1(1), CHAR2(2),
    INTEGER1(1), INTEGER2(2), INTEGER4(4), UNSIGNED_INTEGER1(1), UNSIGNED_INTEGER2(2),
    UNSIGNED_INTEGER4(4), REAL4(4), REAL8(8), COMPLEX8(8), COMPLEX16(16), TIMESTAMP(8), GUID(16);

    private final int byteSize;

    PhysicalType(int byteSize) {
        this.byteSize = byteSize;
    }

    public int getByteSize() {
        return byteSize;
    }
}