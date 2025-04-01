package com.pabu5h.pq_decoder.physical_parser;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.math3.complex.Complex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pabu5h.pq_decoder.util.BitConverter;
import com.pabu5h.pq_decoder.util.DateTime;
import com.pabu5h.pq_decoder.util.GUID;

public class ScalarElement extends Element {

    private byte[] m_value;

    private PhysicalType typeOfValue;
    
    public ElementType typeOfElement = ElementType.SCALAR;

    public ScalarElement() {
        this.m_value = new byte[16];
    }

    // Get type of element
    public ElementType getTypeOfElement() {
        return ElementType.SCALAR;
    }

    public PhysicalType getTypeOfValue() {
        return typeOfValue;
    }

    public void setTypeOfValue(PhysicalType typeOfValue) {
        this.typeOfValue = typeOfValue;
    }
    
	public void setValue(byte[] value, int offset) {
		System.arraycopy(value, offset, m_value, 0, typeOfValue.getByteSize());
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName().replace("Element", "") + " [Tag=" + (getTagOfElement() == null ? null : getTagOfElement().toString()) + ", Type=" + getTypeOfValue() + "]";
	}

    // Method to get the value as per the type
//    public Object get() {
//        switch (typeOfValue) {
//            case BOOLEAN1: return m_value[0] != 0;
//            case BOOLEAN2: return ByteBuffer.wrap(m_value).getShort(0) != 0;
//            case BOOLEAN4: return ByteBuffer.wrap(m_value).getInt(0) != 0;
//            case CHAR1: return new String(m_value, StandardCharsets.US_ASCII).substring(0, 1);
//            case CHAR2: return new String(m_value, StandardCharsets.UTF_16).substring(0, 1);
//            case INTEGER1: return (byte) m_value[0];
//            case INTEGER2: return ByteBuffer.wrap(m_value).getShort(0);
//            case INTEGER4: return ByteBuffer.wrap(m_value).getInt(0);
//            case UNSIGNED_INTEGER1: return m_value[0] & 0xFF;
//            case UNSIGNED_INTEGER2: return ByteBuffer.wrap(m_value).getShort(0) & 0xFFFF;
//            case UNSIGNED_INTEGER4: return ByteBuffer.wrap(m_value).getInt(0) & 0xFFFFFFFFL;
//            case REAL4: return ByteBuffer.wrap(m_value).getFloat(0);
//            case REAL8: return ByteBuffer.wrap(m_value).getDouble(0);
//            case COMPLEX8: return new Complex(ByteBuffer.wrap(m_value).getFloat(0), ByteBuffer.wrap(m_value).getFloat(4));
//            case COMPLEX16: return new Complex(ByteBuffer.wrap(m_value).getDouble(0), ByteBuffer.wrap(m_value).getDouble(8));
//            case TIMESTAMP: return new java.sql.Timestamp(ByteBuffer.wrap(m_value).getLong(0));
//            case GUID: return UUID.nameUUIDFromBytes(m_value);
//            default: throw new IllegalArgumentException("Unknown physical type");
//        }
//    }

    // Method to set value according to type
//    public void set(Object value) {
//        switch (typeOfValue) {
//            case BOOLEAN1: setBoolean1((Boolean) value); break;
//            case BOOLEAN2: setBoolean2((Boolean) value); break;
//            case BOOLEAN4: setBoolean4((Boolean) value); break;
//            case CHAR1: setChar1((Character) value); break;
//            case CHAR2: setChar2((Character) value); break;
//            case INTEGER1: setInteger1((Byte) value); break;
//            case INTEGER2: setInteger2((Short) value); break;
//            case INTEGER4: setInteger4((Integer) value); break;
//            case UNSIGNED_INTEGER1: setUnsignedInteger1((Byte) value); break;
//            case UNSIGNED_INTEGER2: setUnsignedInteger2((Short) value); break;
//            case UNSIGNED_INTEGER4: setUnsignedInteger4((Integer) value); break;
//            case REAL4: setReal4((Float) value); break;
//            case REAL8: setReal8((Double) value); break;
//            case COMPLEX8: setComplex8((Complex) value); break;
//            case COMPLEX16: setComplex16((Complex) value); break;
//            case TIMESTAMP: setTimestamp((java.sql.Timestamp) value); break;
//            case GUID: setGuid((UUID) value); break;
//            default: throw new IllegalArgumentException("Unknown physical type");
//        }
//    }



    // Setters for different types
    private void setBoolean1(Boolean value) {
        m_value[0] = value ? (byte) 1 : (byte) 0;
    }

    private void setBoolean2(Boolean value) {
        ByteBuffer.wrap(m_value).putShort(0, value ? (short) 1 : (short) 0);
    }

    private void setBoolean4(Boolean value) {
        ByteBuffer.wrap(m_value).putInt(0, value ? 1 : 0);
    }

    private void setChar1(Character value) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) value.charValue();
        System.arraycopy(bytes, 0, m_value, 0, 1);
    }

    private void setChar2(Character value) {
        byte[] bytes = new byte[2];
        ByteBuffer.wrap(bytes).putChar(value);
        System.arraycopy(bytes, 0, m_value, 0, 2);
    }

    private void setInteger1(Byte value) {
        m_value[0] = value;
    }

    private void setInteger2(Short value) {
        ByteBuffer.wrap(m_value).putShort(0, value);
    }

    private void setInteger4(Integer value) {
        ByteBuffer.wrap(m_value).putInt(0, value);
    }

    private void setUnsignedInteger1(Byte value) {
        m_value[0] = value;
    }

    private void setUnsignedInteger2(Short value) {
        ByteBuffer.wrap(m_value).putShort(0, value);
    }

    private void setUnsignedInteger4(Integer value) {
        ByteBuffer.wrap(m_value).putInt(0, value);
    }

    private void setReal4(Float value) {
        ByteBuffer.wrap(m_value).putFloat(0, value);
    }

    public void setReal8(Double value) {
        ByteBuffer.wrap(m_value).putDouble(0, value);
    }

    private void setComplex8(Complex value) {
        ByteBuffer.wrap(m_value).putFloat(0, (float) value.getReal());
        ByteBuffer.wrap(m_value).putFloat(4, (float) value.getImaginary());
    }

    private void setComplex16(Complex value) {
        ByteBuffer.wrap(m_value).putDouble(0, value.getReal());
        ByteBuffer.wrap(m_value).putDouble(8, value.getImaginary());
    }

    public void setTimestamp(DateTime value) {
        ByteBuffer.wrap(m_value).putLong(0, value.getTime());
    }

    public void setGuid(GUID value) {
       //  m_value = value.toString().getBytes(StandardCharsets.UTF_8);
    }

    @JsonIgnore
	public int getUInt4() {
		return ByteBuffer.wrap(m_value).order(ByteOrder.LITTLE_ENDIAN).getInt(0);
	}
    
    @JsonIgnore
    public GUID getGuid() {
    	return new GUID(m_value);
    }

    @JsonIgnore
	public DateTime getTimestamp() {
		return DateTime.getTimestamp(m_value);
	}

	public void setUInt4(int value) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	public double getReal8() {
		return BitConverter.toDouble(m_value);
	}

	public void setReal8(int value) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	public boolean getBool4() {
		return BitConverter.toUInt32(m_value) != 0;
	}

	public void setBool4(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	public short getInt2() {
		return (short) BitConverter.toUInt2(m_value);
	}

	public void setInt2(int value) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	public Object get() {
		// TODO Auto-generated method stub
		return m_value;
	}
}

