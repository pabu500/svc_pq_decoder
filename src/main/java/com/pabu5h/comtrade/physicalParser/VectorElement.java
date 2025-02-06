package com.pabu5h.comtrade.physicalParser;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.math3.complex.Complex;

@Entity
public class VectorElement extends Element {  // Extend Element

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int size; // The size of the vector
    private int byteSize;

    @ElementCollection
    private List<byte[]> m_values = new ArrayList<>(); // List of byte[] for vector values

    @Enumerated(EnumType.STRING)
    private PhysicalType typeOfValue;

    // Enum for types
    public enum PhysicalType {
        BOOLEAN1, BOOLEAN2, BOOLEAN4, CHAR1, CHAR2,
        INTEGER1, INTEGER2, INTEGER4, UNSIGNED_INTEGER1, UNSIGNED_INTEGER2,
        UNSIGNED_INTEGER4, REAL4, REAL8, COMPLEX8, COMPLEX16, TIMESTAMP, GUID
    }

    // Default constructor for JPA
    public VectorElement() {
        // JPA requires a no-argument constructor
    }

    @Override
    public ElementType getTypeOfElement() {
        return ElementType.VECTOR;  // Return the appropriate enum value
    }



    // Constructor
    public VectorElement(List<byte[]> values) {
        this.m_values = values;
        if (!values.isEmpty()) {
            this.typeOfValue = determineTypeOfValue(values.get(0));
        }
    }

    // Determine type of value (simple assumption here, you can enhance this logic)
    private PhysicalType determineTypeOfValue(byte[] value) {
        return PhysicalType.BOOLEAN1; // For simplicity, default to BOOLEAN1
    }

    // Get value based on type
    public List<Object> get() {
        List<Object> result = new ArrayList<>();
        for (byte[] value : m_values) {
            result.add(getValueByType(value));
        }
        return result;
    }

    private Object getValueByType(byte[] value) {
        switch (typeOfValue) {
            case BOOLEAN1: return value[0] != 0;
            case BOOLEAN2: return ByteBuffer.wrap(value).getShort(0) != 0;
            case BOOLEAN4: return ByteBuffer.wrap(value).getInt(0) != 0;
            case CHAR1: return new String(value, StandardCharsets.US_ASCII).substring(0, 1);
            case CHAR2: return new String(value, StandardCharsets.UTF_16).substring(0, 1);
            case INTEGER1: return (byte) value[0];
            case INTEGER2: return ByteBuffer.wrap(value).getShort(0);
            case INTEGER4: return ByteBuffer.wrap(value).getInt(0);
            case UNSIGNED_INTEGER1: return value[0] & 0xFF;
            case UNSIGNED_INTEGER2: return ByteBuffer.wrap(value).getShort(0) & 0xFFFF;
            case UNSIGNED_INTEGER4: return ByteBuffer.wrap(value).getInt(0) & 0xFFFFFFFFL;
            case REAL4: return ByteBuffer.wrap(value).getFloat(0);
            case REAL8: return ByteBuffer.wrap(value).getDouble(0);
            case COMPLEX8: return new Complex(ByteBuffer.wrap(value).getFloat(0), ByteBuffer.wrap(value).getFloat(4));
            case COMPLEX16: return new Complex(ByteBuffer.wrap(value).getDouble(0), ByteBuffer.wrap(value).getDouble(8));
            case TIMESTAMP: return new Timestamp(ByteBuffer.wrap(value).getLong(0));
            case GUID: return UUID.nameUUIDFromBytes(value);
            default: throw new IllegalArgumentException("Unknown physical type");
        }
    }

    // Set value for the vector elements
    public void set(List<Object> values) {
        if (values.size() != m_values.size()) {
            throw new IllegalArgumentException("Size mismatch between values and current vector length.");
        }

        for (int i = 0; i < m_values.size(); i++) {
            setValueByType(m_values.get(i), values.get(i));
        }
    }

    private void setValueByType(byte[] value, Object objectValue) {
        switch (typeOfValue) {
            case BOOLEAN1:
                setBoolean1(value, (Boolean) objectValue);
                break;
            case BOOLEAN2:
                setBoolean2(value, (Boolean) objectValue);
                break;
            case BOOLEAN4:
                setBoolean4(value, (Boolean) objectValue);
                break;
            case CHAR1:
                setChar1(value, (Character) objectValue);
                break;
            case CHAR2:
                setChar2(value, (Character) objectValue);
                break;
            case INTEGER1:
                setInteger1(value, (Byte) objectValue);
                break;
            case INTEGER2:
                setInteger2(value, (Short) objectValue);
                break;
            case INTEGER4:
                setInteger4(value, (Integer) objectValue);
                break;
            case UNSIGNED_INTEGER1:
                setUnsignedInteger1(value, (Byte) objectValue);
                break;
            case UNSIGNED_INTEGER2:
                setUnsignedInteger2(value, (Short) objectValue);
                break;
            case UNSIGNED_INTEGER4:
                setUnsignedInteger4(value, (Integer) objectValue);
                break;
            case REAL4:
                setReal4(value, (Float) objectValue);
                break;
            case REAL8:
                setReal8(value, (Double) objectValue);
                break;
            case COMPLEX8:
                setComplex8(value, (Complex) objectValue);
                break;
            case COMPLEX16:
                setComplex16(value, (Complex) objectValue);
                break;
            case TIMESTAMP:
                setTimestamp(value, (Timestamp) objectValue);
                break;
            case GUID:
                setGuid(value, (UUID) objectValue);
                break;
            default:
                throw new IllegalArgumentException("Unknown physical type");
        }
    }

    // Setters for different types (for vector elements)
    private void setBoolean1(byte[] value, Boolean v) {
        value[0] = v ? (byte) 1 : (byte) 0;
    }

    private void setBoolean2(byte[] value, Boolean v) {
        ByteBuffer.wrap(value).putShort(0, v ? (short) 1 : (short) 0);
    }

    private void setBoolean4(byte[] value, Boolean v) {
        ByteBuffer.wrap(value).putInt(0, v ? 1 : 0);
    }

    private void setChar1(byte[] value, Character v) {
        value[0] = (byte) v.charValue();
    }

    private void setChar2(byte[] value, Character v) {
        ByteBuffer.wrap(value).putChar(0, v);
    }

    private void setInteger1(byte[] value, Byte v) {
        value[0] = v;
    }

    private void setInteger2(byte[] value, Short v) {
        ByteBuffer.wrap(value).putShort(0, v);
    }

    private void setInteger4(byte[] value, Integer v) {
        ByteBuffer.wrap(value).putInt(0, v);
    }

    private void setUnsignedInteger1(byte[] value, Byte v) {
        value[0] = v;
    }

    private void setUnsignedInteger2(byte[] value, Short v) {
        ByteBuffer.wrap(value).putShort(0, v);
    }

    private void setUnsignedInteger4(byte[] value, Integer v) {
        ByteBuffer.wrap(value).putInt(0, v);
    }

    private void setReal4(byte[] value, Float v) {
        ByteBuffer.wrap(value).putFloat(0, v);
    }

    private void setReal8(byte[] value, Double v) {
        ByteBuffer.wrap(value).putDouble(0, v);
    }

    private void setComplex8(byte[] value, Complex v) {
        ByteBuffer.wrap(value).putFloat(0, (float) v.getReal());
        ByteBuffer.wrap(value).putFloat(4, (float) v.getImaginary());
    }

    private void setComplex16(byte[] value, Complex v) {
        ByteBuffer.wrap(value).putDouble(0, v.getReal());
        ByteBuffer.wrap(value).putDouble(8, v.getImaginary());
    }

    private void setTimestamp(byte[] value, Timestamp v) {
        ByteBuffer.wrap(value).putLong(0, v.getTime());
    }

    private void setGuid(byte[] value, UUID v) {
        System.arraycopy(v.toString().getBytes(StandardCharsets.UTF_8), 0, value, 0, value.length);
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTypeOfValue(PhysicalType typeOfValue) {
        this.typeOfValue = typeOfValue;
    }
}
