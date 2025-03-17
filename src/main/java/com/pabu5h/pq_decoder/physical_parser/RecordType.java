package com.pabu5h.pq_decoder.physical_parser;

public enum RecordType {
	//
	// Summary:
	// Represents a record level tag which identifies the container record. Always
	// the
	// first one in the file, and only one per file.
	Container,
	//
	// Summary:
	// Represents a record level tag which identifies the data source record.
	// Instrument
	// level information.
	DataSource,
	//
	// Summary:
	// Represents an optional record level tag which identifies configuration
	// parameters.
	// Used to capture configuration changes on the data source.
	MonitorSettings,
	//
	// Summary:
	// Represents a record-level tag which identifies an observation. Used to
	// capture
	// an event, measurements etc.
	Observation,
	//
	// Summary:
	// Represents a record-level tag which identifies a blank record.
	Blank,
	//
	// Summary:
	// Represents a record-level tag which is unknown to this library.
	Unknown
}