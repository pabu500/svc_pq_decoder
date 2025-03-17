package com.pabu5h.pq_decoder.physical_parser;

public enum CompressionStyle {
	//
	// Summary:
	// No compression.
	None,
	//
	// Summary:
	// Compress the entire file after the container record. This compression style
	// is
	// deprecated and is currently not supported by this PQDIF library.
	TotalFile,
	//
	// Summary:
	// Compress the body of each record.
	RecordLevel;
	
	public static CompressionStyle from(int vl) {
		if (vl == 1) {
			return TotalFile;
		}
		
		if (vl == 2) {
			return RecordLevel;
		}
		
		return None;
	}
}