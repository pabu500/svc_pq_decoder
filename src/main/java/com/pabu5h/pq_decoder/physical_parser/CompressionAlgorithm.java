package com.pabu5h.pq_decoder.physical_parser;

public enum CompressionAlgorithm {
	//
	// Summary:
	// No compression.
	// None = 0u, -> 0x0
	None,
	//
	// Summary:
	// Zlib compression. http://www.zlib.net/
	// Zlib = 1u, 0x1
	Zlib,
	//
	// Summary:
	// PKZIP compression. This compression algorithm is deprecated and is currently
	// not supported by this PQDIF library.
	// PKZIP = 0x40u -> 0x40 int -> 64 int
	PKZIP;
	
	public static CompressionAlgorithm from(int vl) {
		if (vl == 1) {
			return Zlib;
		}
		
		if (vl == 64) {
			return PKZIP;
		}
		
		return None;
	}
}