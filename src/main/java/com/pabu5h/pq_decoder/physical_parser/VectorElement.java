package com.pabu5h.pq_decoder.physical_parser;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import com.pabu5h.pq_decoder.util.GUID;

public class VectorElement extends Element {  // Extend Element

    private int m_Size;

    private byte[] m_value; // List of byte[] for vector values

//    @Enumerated(EnumType.STRING)
    private PhysicalType typeOfValue;

//    // Enum for types
//    public enum PhysicalType {
//        BOOLEAN1, BOOLEAN2, BOOLEAN4, CHAR1, CHAR2,
//        INTEGER1, INTEGER2, INTEGER4, UNSIGNED_INTEGER1, UNSIGNED_INTEGER2,
//        UNSIGNED_INTEGER4, REAL4, REAL8, COMPLEX8, COMPLEX16, TIMESTAMP, GUID
//    }

    // Default constructor for JPA
    public VectorElement() {
        // JPA requires a no-argument constructor
    }

    public PhysicalType getTypeOfValue() {
        return typeOfValue;
    }

    @Override
    public ElementType getTypeOfElement() {
        return ElementType.VECTOR;  // Return the appropriate enum value
    }



    // Constructor
    public VectorElement(byte[] value) {
        this.m_value = value;
        if (value != null) {
            this.typeOfValue = determineTypeOfValue(value);
        }
    }

    // Determine type of value (simple assumption here, you can enhance this logic)
    private PhysicalType determineTypeOfValue(byte[] value) {
        return PhysicalType.Boolean1; // For simplicity, default to BOOLEAN1
    }

    // Get value based on type
    public List<Object> get() {
        List<Object> result = new ArrayList<>();
//        for (byte[] value : m_values) {
//            result.add(getValueByType(value));
//        }
        return result;
    }
    
	@Override
	public String toString() {
		return getClass().getSimpleName().replace("Element", "") + " [Tag=" + (getTagOfElement() == null ? null : getTagOfElement().toString()) + ", Type=" + getTypeOfValue() + ", Size=" + m_Size + "]";
	}
    

    private Object getValueByType(byte[] value) {
        switch (typeOfValue) {
            case Boolean1: return value[0] != 0;
            case Boolean2: return ByteBuffer.wrap(value).getShort(0) != 0;
            case Boolean4: return ByteBuffer.wrap(value).getInt(0) != 0;
            case Char1: return new String(value, StandardCharsets.US_ASCII).substring(0, 1);
            case Char2: return new String(value, StandardCharsets.UTF_16).substring(0, 1);
            case Integer1: return (byte) value[0];
            case Integer2: return ByteBuffer.wrap(value).getShort(0);
            case Integer4: return ByteBuffer.wrap(value).getInt(0);
            case UnsignedInteger1: return value[0] & 0xFF;
            case UnsignedInteger2: return ByteBuffer.wrap(value).getShort(0) & 0xFFFF;
            case UnsignedInteger4: return ByteBuffer.wrap(value).getInt(0) & 0xFFFFFFFFL;
            case Real4: return ByteBuffer.wrap(value).getFloat(0);
            case Real8: return ByteBuffer.wrap(value).getDouble(0);
            case Complex8: return new Complex(ByteBuffer.wrap(value).getFloat(0), ByteBuffer.wrap(value).getFloat(4));
            case Complex16: return new Complex(ByteBuffer.wrap(value).getDouble(0), ByteBuffer.wrap(value).getDouble(8));
            case Timestamp: return new Timestamp(ByteBuffer.wrap(value).getLong(0));
            case Guid: return new GUID(value);
            default: throw new IllegalArgumentException("Unknown physical type");
        }
    }

    // Set value for the vector elements
    public void set(List<Object> values) {
//        if (values.size() != m_values.size()) {
//            throw new IllegalArgumentException("Size mismatch between values and current vector length.");
//        }
//
//        for (int i = 0; i < m_values.size(); i++) {
//            setValueByType(m_values.get(i), values.get(i));
//        }
    }
    
	public void setValues(byte[] value, int offset) {
		if (m_value == null) {
			m_value = new byte[m_Size * typeOfValue.getByteSize()];
		}
		System.arraycopy(value, offset, m_value, 0, m_Size * typeOfValue.getByteSize());
	}

    private void setValueByType(byte[] value, Object objectValue) {
        switch (typeOfValue) {
            case Boolean1:
                setBoolean1(value, (Boolean) objectValue);
                break;
            case Boolean2:
                setBoolean2(value, (Boolean) objectValue);
                break;
            case Boolean4:
                setBoolean4(value, (Boolean) objectValue);
                break;
            case Char1:
                setChar1(value, (Character) objectValue);
                break;
            case Char2:
                setChar2(value, (Character) objectValue);
                break;
            case Integer1:
                setInteger1(value, (Byte) objectValue);
                break;
            case Integer2:
                setInteger2(value, (Short) objectValue);
                break;
            case Integer4:
                setInteger4(value, (Integer) objectValue);
                break;
            case UnsignedInteger1:
                setUnsignedInteger1(value, (Byte) objectValue);
                break;
            case UnsignedInteger2:
                setUnsignedInteger2(value, (Short) objectValue);
                break;
            case UnsignedInteger4:
                setUnsignedInteger4(value, (Integer) objectValue);
                break;
            case Real4:
                setReal4(value, (Float) objectValue);
                break;
            case Real8:
                setReal8(value, (Double) objectValue);
                break;
            case Complex8:
                setComplex8(value, (Complex) objectValue);
                break;
            case Complex16:
                setComplex16(value, (Complex) objectValue);
                break;
            case Timestamp:
                setTimestamp(value, (Timestamp) objectValue);
                break;
            case Guid:
                setGuid(value, (GUID) objectValue);
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

    private void setGuid(byte[] value, GUID v) {
        System.arraycopy(v.toString().getBytes(StandardCharsets.UTF_8), 0, value, 0, value.length);
    }

    public int getByteSize() {
        return m_Size;
    }

    public void setSize(int size) {
        this.m_Size = size;
    }

    public void setTypeOfValue(PhysicalType typeOfValue) {
        this.typeOfValue = typeOfValue;
    }
}
