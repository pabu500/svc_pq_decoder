package com.pabu5h.pq_decoder.util;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * ~ C# Guid & java UUID
 */
public final class GUID {

	public static final GUID Empty = new GUID();

	private final long mostSigBits;

	private final long leastSigBits;

	public GUID() {
		this("00000000-0000-0000-0000-000000000000");
	}

	public GUID(long mostSigBits, long leastSigBits) {
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}
	
	public GUID(String name) {
		this(UUID.fromString(inverse(name)));
	}

	public GUID(byte[] data) {
		this(bytesToUUID(data));
	}

	public GUID(UUID uuid) {
		if (uuid == null) {
			throw new IllegalArgumentException("uuid invalid");
		}
		this.mostSigBits = uuid.getMostSignificantBits();
		this.leastSigBits = uuid.getLeastSignificantBits();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (leastSigBits ^ (leastSigBits >>> 32));
		result = prime * result + (int) (mostSigBits ^ (mostSigBits >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GUID other = (GUID) obj;
		if (leastSigBits != other.leastSigBits)
			return false;
		if (mostSigBits != other.mostSigBits)
			return false;
		return true;
	}

	public boolean equals(UUID obj) {
		if (obj == null)
			return false;
		if (leastSigBits != obj.getLeastSignificantBits())
			return false;
		if (mostSigBits != obj.getMostSignificantBits())
			return false;
		return true;
	}

	public String toString() {
		return inverse(new UUID(mostSigBits, leastSigBits).toString());
	}

	public static GUID fromString(String name) {
		return new GUID(name);
	}

	public static GUID randomUUID() {
		return new GUID(UUID.randomUUID());
	}

	private static String inverse(String name) {
		if (name.length() != 36) {
			assert name != null && name.length() == 36 : "name must be 36 char in length";
		}
		StringBuilder rs = new StringBuilder();
		rs.append(name.substring(6, 8));
		rs.append(name.substring(4, 6));
		rs.append(name.substring(2, 4));
		rs.append(name.substring(0, 2));
		rs.append('-');
		rs.append(name.substring(11, 13));
		rs.append(name.substring(9, 11));
		rs.append('-');
		rs.append(name.substring(16, 18));
		rs.append(name.substring(14, 16));
		rs.append('-');
		rs.append(name.substring(19));
		return rs.toString();
	}

	private static UUID bytesToUUID(byte[] data) {
		long msb = 0;
		long lsb = 0;
		assert data.length == 16 : "data must be 16 bytes in length";
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (data[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (data[i] & 0xff);
		return new UUID(msb, lsb);
	}
	
	public static class GUIDSerializer extends JsonSerializer<GUID> {
		@Override
		public void serialize(GUID value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			if (value == null) {
	            gen.writeNull();
	        } else {
	            gen.writeString(value.toString());
	        }
		}
	}
}