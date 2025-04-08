package com.pabu5h.pq_decoder.physical_parser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import com.pabu5h.pq_decoder.util.DateTime;
import com.pabu5h.pq_decoder.util.GUID;

public class VectorElement extends Element {  // Extend Element

    private int m_Size;

    private byte[] m_value; // List of byte[] for vector values

    public ElementType typeOfElement = ElementType.VECTOR;

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
	
    public Object get(int index)
    {
        switch (typeOfValue)
        {
            case Boolean1:
                return getUInt1(index) != 0;

            case Boolean2:
                return getInt2(index) != 0;

            case Boolean4:
                return getInt4(index) != 0;

            case Char1:
				return /* Encoding.ASCII.GetString(m_values)[index] */new String(new byte[] {m_value[index]}, StandardCharsets.US_ASCII);

            case Char2:
				return /* Encoding.Unicode.GetString(m_values)[index] */new String(new byte[] {m_value[index]}, StandardCharsets.UTF_8);

            case Integer1:
                return getInt1(index);

            case Integer2:
                return getInt2(index);

            case Integer4:
                return getInt4(index);

            case UnsignedInteger1:
                return getUInt1(index);

            case UnsignedInteger2:
                return getUInt2(index);

            case UnsignedInteger4:
                return getUInt4(index);

            case Real4:
                return getReal4(index);

            case Real8:
                return getReal8(index);

            case Complex8:
                return new ComplexNumber(getReal4(index * 2).doubleValue(), getReal4(index * 2 + 1).doubleValue());

            case Complex16:
                return new ComplexNumber(getReal8(index * 2), getReal8(index * 2 + 1));

            case Timestamp:
                return getTimestamp(index);

            case Guid:
            	
            	byte[] tmp = new byte[16];
            	System.arraycopy(tmp, 0, m_value, index * 16, tmp.length);
				return new GUID(/* m_values.BlockCopy(index * 16, 16) */tmp);

            default:
                throw new IndexOutOfBoundsException();
        }
    }
    
	public byte getUInt1(int index) {
		return (byte) m_value[index];
	}
	
	public byte getInt1(int index) {
		return (byte) m_value[index];
	}
	
    /// <summary>
    /// Gets a value in this vector as a 16-bit unsigned integer.
    /// </summary>
    /// <param name="index">The index of the value.</param>
    /// <returns>The value as a 16-bit unsigned integer.</returns>
	public int getInt2(int index) {
		
		byte[] tmp = new byte[16];
		int byteIndex = index * 2;
		System.arraycopy(m_value, byteIndex, tmp, 0, 16);
		return ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
    /// <summary>
    /// Gets a value in this vector as a 16-bit unsigned integer.
    /// </summary>
    /// <param name="index">The index of the value.</param>
    /// <returns>The value as a 16-bit unsigned integer.</returns>
	public int getUInt2(int index) {
		
		byte[] tmp = new byte[16];
		int byteIndex = index * 2;
		System.arraycopy(m_value, byteIndex, tmp, 0, 16);
		return ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	public int getInt4(int index) {
		
		byte[] tmp = new byte[32];
		int byteIndex = index * 4;
		System.arraycopy(m_value, byteIndex, tmp, 0, 32);
		return ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
    /// <summary>
    /// Gets a value in this vector as a 32-bit unsigned integer.
    /// </summary>
    /// <param name="index">The index of the value.</param>
    /// <returns>The value as a 32-bit unsigned integer.</returns>
	public int getUInt4(int index) {
		
		byte[] tmp = new byte[32];
		int byteIndex = index * 4;
		System.arraycopy(m_value, byteIndex, tmp, 0, 32);
		return ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
    
	
    /// <summary>
    /// Gets a value in this vector as a 32-bit floating point number.
    /// </summary>
    /// <param name="index">The index of the value.</param>
    /// <returns>The value as a 32-bit floating point number.</returns>
    public BigDecimal getReal4(int index) {
		byte[] tmp = new byte[32];
		int byteIndex = index * 4;
		System.arraycopy(m_value, byteIndex, tmp, 0, 32);
		
		double tmpdb = ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getDouble();
		return new BigDecimal(tmpdb).setScale(tmpdb == 0d ? 0 : 20, RoundingMode.HALF_UP);
    }
    
    public BigDecimal getReal8(int index) {
		int byteIndex = index * 8;
		byte[] tmp = new byte[64];
		
		try {
			
			int copyLength = (m_value.length - 1 - byteIndex) >= tmp.length ? tmp.length : (m_value.length - 1 - byteIndex);
			System.arraycopy(m_value, byteIndex, tmp, 0, copyLength);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		double tmpdb = ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getDouble();
		return new BigDecimal(tmpdb).setScale(tmpdb == 0d ? 0 : 20, RoundingMode.HALF_UP);
    }
    
    public DateTime getTimestamp(int index) {
        if (m_value == null)
            throw new RuntimeException("Unable to retrieve values from uninitialized vector; set the size and physical type of the vector first");

        int byteIndex = index * 12;

        DateTime epoch = new DateTime(1900, 1, 1);
        int days = getInt4(index); // LittleEndian.ToUInt32(m_values, byteIndex);
        
        byte[] tmp = new byte[m_value.length - (byteIndex + 4)];
        System.arraycopy(m_value, byteIndex + 4, tmp, 0, tmp.length);
        double seconds = ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getDouble();//LittleEndian.ToDouble(m_values, byteIndex + 4);

        // Timestamps in a PQDIF file are represented by two separate numbers, one being the number of
        // days since January 1, 1900 and the other being the number of seconds since midnight. The
        // standard implementation also includes a constant for the number of days between January 1,
        // 1900 and January 1, 1970 to facilitate the conversion between PQDIF timestamps and UNIX
        // timestamps. However, the constant defined in the standard is 25569 days, whereas the actual
        // number of days between those two dates is 25567 days; a two day difference. That is why we
        // need to also subtract two days here when parsing PQDIF timestamps.
        return epoch.addDays(days - 2).addSeconds(seconds);
    }
    
    public static class ComplexNumber {
    	public Object Real;
    	public Object Imaginary;
    	public ComplexNumber(Number nb1, Number nb2) {
    		Real = nb1;
    		Imaginary = nb2;
    	}
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
    
    public void setUInt4(int value, int v) {
//        ByteBuffer.wrap(value).putInt(0, v);
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

    public int getSize() {
        return m_Size;
    }

    public void setSize(int size) {
        this.m_Size = size;
    }

    public void setTypeOfValue(PhysicalType typeOfValue) {
        this.typeOfValue = typeOfValue;
    }

	public byte[] getValues() {
		return m_value;
	}

//	public double getUInt4(int i) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	public void set(int i, Object start) {
		// TODO Auto-generated method stub
		
	}

//	public Object get(int i) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
