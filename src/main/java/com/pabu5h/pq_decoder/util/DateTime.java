package com.pabu5h.pq_decoder.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTime extends Date {

	public static final DateTime MinValue = new DateTime(1, 1, 1);
	
	int year;
	int month;
	int day;

	int h;
	int m;
	int s;
	int n;

	public DateTime(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		
		toSysTime();
	}

	public DateTime() {
		toSysTime();
	}
	
	public DateTime(int year, int month, int day, int h, int m, int s) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.h = h;
		this.m = m;
		this.s = s;
		
		toSysTime();
	}
	
	public DateTime addDays(int day) {
		this.day += day;
		
		toSysTime();
		
		return this;
	}
	
	public DateTime addSeconds(Number seconds) {
		this.s += seconds.intValue();
		
		toSysTime();
		
		return this;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
		
		toSysTime();
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
		
		toSysTime();
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
		
		toSysTime();
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
		
		toSysTime();
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
		
		toSysTime();
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
		
		toSysTime();
	}
	
	public static DateTime utcNow() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		DateTime utc = new DateTime();
		utc.year = c.get(Calendar.YEAR);
		utc.month = c.get(Calendar.MONTH) + 1;
		utc.day = c.get(Calendar.DAY_OF_MONTH);
		
		utc.h = c.get(Calendar.HOUR_OF_DAY);
		utc.m = c.get(Calendar.MINUTE);
		utc.s = c.get(Calendar.SECOND);
		
		return utc;
	}

	public Date toSysTime() {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);
		c.set(Calendar.SECOND, s);
		
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH) + 1;
		this.day = c.get(Calendar.DAY_OF_MONTH);
		
		this.h = c.get(Calendar.HOUR_OF_DAY);
		this.m = c.get(Calendar.MINUTE);
		this.s = c.get(Calendar.SECOND);
		
		return c.getTime();
	}
	
	public String toString() {
		return new SimpleDateFormat().format(toSysTime());
	}
	
    public static DateTime getTimestamp(byte[] m_value)  {
    	
//    	m_value = new byte[] {
//    			(byte) 164, 
//    			(byte) 175, 
//    			(byte) 0, 
//    			(byte) 0, 
//    			(byte) 115, 
//    			(byte) 104, 
//    			(byte) 145, 
//    			(byte) 237, 
//    			(byte) 132,
//    			(byte) 120,
//    			(byte) 244,
//    			(byte) 64
//    			};
//        Span<byte> span = m_value.AsSpan();m_value
//        Span<byte> span2 = span.Slice(0, 4);m_value[0->3]
//        span = m_value.AsSpan();
//        Span<byte> span3 = span.Slice(span2.Length, 8); m_value[4->11]
//        if (!BitConverter.IsLittleEndian)
//        {
//            span2.Reverse();
//            span3.Reverse();
//        }
//
//        uint num = BitConverter.ToUInt32(span2);
//        double value = BitConverter.ToDouble(span3);
//        return new DateTime(1900, 1, 1).AddDays(num - 2).AddSeconds(value);
    	
    	byte[] span = new byte[m_value.length];
    	System.arraycopy(m_value, 0, span, 0, m_value.length);
    	
    	byte[] span2 = new byte[4];
    	System.arraycopy(span, 0, span2, 0, span2.length);
    	
    	byte[] span3 = new byte[8];
    	System.arraycopy(span, span2.length, span3, 0, 8);
    	
    	double value = ByteBuffer.wrap(span3).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    	
    	int num = ByteBuffer.wrap(span2).order(ByteOrder.LITTLE_ENDIAN).getInt();
    	
    	return new DateTime(1900, 1, 1).addDays(num - 2).addSeconds(value);
    }
	
	public static void main(String[] args) {
		System.out.println(DateTime.getTimestamp(null));
	}
}
