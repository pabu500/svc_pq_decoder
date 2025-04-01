package com.pabu5h.pq_decoder.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitConverter {

	public static int toUInt32(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	public static int toUInt2(byte[] bytes) {
		byte[] tmp = new byte[2];
		System.arraycopy(tmp, 0, bytes, 0, 2);
		return ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	public static double toDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getDouble();
	}
}
