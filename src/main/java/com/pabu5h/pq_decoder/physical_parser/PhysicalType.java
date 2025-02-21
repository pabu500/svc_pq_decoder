package com.pabu5h.pq_decoder.physical_parser;

public enum PhysicalType {
	 /// <summary>
    /// 1-byte boolean
    /// </summary>
    Boolean1(1),

    /// <summary>
    /// 2-byte boolean
    /// </summary>
    Boolean2(2),

    /// <summary>
    /// 4-byte boolean
    /// </summary>
    Boolean4(3),

    /// <summary>
    /// 1-byte character (ASCII)
    /// </summary>
    Char1(10),

    /// <summary>
    /// 2-byte character (UTF-16)
    /// </summary>
    Char2(11),

    /// <summary>
    /// 8-bit signed integer
    /// </summary>
    Integer1(20),

    /// <summary>
    /// 16-bit signed integer
    /// </summary>
    Integer2(21),

    /// <summary>
    /// 32-bit signed integer
    /// </summary>
    Integer4(22),

    /// <summary>
    /// 8-bit unsigned integer
    /// </summary>
    UnsignedInteger1(30),

    /// <summary>
    /// 16-bit unsigned integer
    /// </summary>
    UnsignedInteger2(31),

    /// <summary>
    /// 32-bit unsigned integer
    /// </summary>
    UnsignedInteger4(32),

    /// <summary>
    /// 32-bit floating point number
    /// </summary>
    Real4(40),

    /// <summary>
    /// 64-bit floating point number
    /// </summary>
    Real8(41),

    /// <summary>
    /// 8-byte complex number
    /// </summary>
    /// <remarks>
    /// The first four bytes represent the real part of the complex
    /// number, and the last four bytes represent the imaginary part.
    /// Both values are 32-bit floating point numbers.
    /// </remarks>
    Complex8(42),

    /// <summary>
    /// 16-byte complex number
    /// </summary>
    /// <remarks>
    /// The first eight bytes represent the real part of the complex
    /// number, and the last eight bytes represent the imaginary part.
    /// Both values are 64-bit floating point numbers.
    /// </remarks>
    Complex16(43),

    /// <summary>
    /// 12-byte timestamp
    /// </summary>
    /// <remarks>
    /// The first four bytes represent the days since January 1, 1900
    /// UTC. The last eight bytes represent the number of seconds since
    /// midnight. The number of days is an unsigned 32-bit integer, and
    /// the number of seconds is a 64-bit floating point number.
    /// </remarks>
    Timestamp(50),

    /// <summary>
    /// 128-bit globally unique identifier
    /// </summary>
    Guid(60);

    private final int byteS;

    PhysicalType(int byteS) {
        this.byteS = byteS;
    }

    public int getSize() {
        return byteS;
    }
    
	public int getByteSize() {
		switch (this) {
		case Boolean1:
		case Char1:
		case Integer1:
		case UnsignedInteger1:
			return 1;
		case Boolean2:
		case Char2:
		case Integer2:
		case UnsignedInteger2:
			return 2;
		case Boolean4:
		case Integer4:
		case UnsignedInteger4:
		case Real4:
			return 4;
		case Real8:
		case Complex8:
			return 8;
		case Timestamp:
			return 12;
		case Complex16:
		case Guid:
			return 16;
		default:
			throw new IllegalArgumentException("type");
		}
	}
    
    public static PhysicalType fromValue(byte value) {
        for (PhysicalType type : PhysicalType.values()) {
            if (type.getSize() == value) {
                return type;
            }
        }
        return null;
    }
}