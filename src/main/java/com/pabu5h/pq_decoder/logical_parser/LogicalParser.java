package com.pabu5h.pq_decoder.logical_parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.stereotype.Component;

import com.pabu5h.pq_decoder.physical_parser.PhysicalParser;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;

//@Component
public class LogicalParser {
    Logger logger = Logger.getLogger(LogicalParser.class.getName());


    private PhysicalParser m_physicalParser;
    private ContainerRecord m_containerRecord;
    private DataSourceRecord m_currentDataSourceRecord;
    private MonitorSettingsRecord m_currentMonitorSettingsRecord;
    private ObservationRecord m_nextObservationRecord;
    private List<DataSourceRecord> m_allDataSourceRecords;

    /// <summary>
    /// Creates a new instance of the <see cref="LogicalParser"/> class.
    /// </summary>
    /// <param name="filePath">Path to the PQDIF file to be parsed.</param>
    public LogicalParser(String filePath)
    {
        m_physicalParser = new PhysicalParser(filePath);
        m_allDataSourceRecords = new ArrayList<>();
    }

    /// <summary>
    /// Creates a new instance of the <see cref="LogicalParser"/> class.
    /// </summary>
    /// <param name="stream">The stream containing the PQDIF file data.</param>
    /// <param name="leaveOpen">True if the stream should be closed when the parser is closed; false otherwise.</param>
    /// <exception cref="ArgumentNullException"><paramref name="stream"/> is null.</exception>
    /// <exception cref="InvalidOperationException"><paramref name="stream"/> is not both readable and seekable.</exception>
    public LogicalParser(InputStream stream, boolean leaveOpen)
    {
        m_physicalParser = new PhysicalParser(null);
        m_allDataSourceRecords = new ArrayList<DataSourceRecord>();
        try {
			open(stream, leaveOpen);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    /// <summary>
    /// Gets or sets the file path (not just the name) of the PQDIF file to be parsed.
    /// Obsolete in favor of <see cref="FilePath"/>.
    /// </summary>
    String FileName;
    public String getFileName() {
		return new File(m_physicalParser.getFilePath()).getName();
	}

	public void setFileName(String fileName) {
		// m_physicalParser.setFileName(fileName);
	}
    
//    public String FileName
//    {
//        get
//        {
//            return m_physicalParser.FilePath;
//        }
//        set
//        {
//            m_physicalParser.FilePath = value;
//        }
//    }

    /// <summary>
    /// Gets or sets the file path of the PQDIF file to be parsed.
    /// </summary>
    


	public String getFilePath() {
		return m_physicalParser.getFilePath();
	}

	public void setFilePath(String filePath) {
		m_physicalParser.setFilePath(filePath);
	}
    
//    public String FilePath
//    {
//        get
//        {
//            return m_physicalParser.FilePath;
//        }
//        set
//        {
//            m_physicalParser.FilePath = value;
//        }
//    }




	/// <summary>
    /// Gets the container record from the PQDIF file. This is
    /// parsed as soon as the parser is <see cref="Open()"/>ed.
    /// </summary>
    public ContainerRecord getContainerRecord()
    {
    	return m_containerRecord;
    }

    /// <summary>
    /// Gets a list of all DataSource records from the PQDIF file. This is
    /// parsed when passing throug the observation records <see cref="NextObservationRecord()"/>ed.
    /// </summary>
    public List<DataSourceRecord> getDataSourceRecords()
    {
    	return m_allDataSourceRecords;
    }

    /// <summary>
    /// Opens the parser and parses the <see cref="ContainerRecord"/>.
    /// </summary>
    /// <exception cref="InvalidOperationException"><see cref="FilePath"/> has not been defined.</exception>
    /// <exception cref="NotSupportedException">An unsupported compression mode was defined in the PQDIF file.</exception>
    public void open() throws Exception
    {
        //m_physicalParser.Open();
        m_physicalParser.openAsync().get();
        m_containerRecord = ContainerRecord.createContainerRecord(m_physicalParser.getNextRecord());
        m_physicalParser.compressionAlgorithm = m_containerRecord.getCompressionAlgorithm();
        m_physicalParser.compressionStyle = m_containerRecord.getCompressionStyle();
    }

    /// <summary>
    /// Opens the parser and parses the <see cref="ContainerRecord"/>.
    /// </summary>
    /// <param name="stream">The stream containing the PQDIF file data.</param>
    /// <param name="leaveOpen">True if the stream should be closed when the parser is closed; false otherwise.</param>
    /// <exception cref="ArgumentNullException"><paramref name="stream"/> is null.</exception>
    /// <exception cref="InvalidOperationException"><paramref name="stream"/> is not both readable and seekable.</exception>
    /// <exception cref="NotSupportedException">An unsupported compression mode was defined in the PQDIF file.</exception>
    public void open(InputStream stream, boolean leaveOpen) throws Exception
    {
        //m_physicalParser.open(stream, leaveOpen);
    	m_physicalParser.openAsync().get();
        m_containerRecord = ContainerRecord.createContainerRecord(m_physicalParser.getNextRecord());
        m_physicalParser.compressionAlgorithm = m_containerRecord.getCompressionAlgorithm();
        m_physicalParser.compressionStyle = m_containerRecord.getCompressionStyle();
    }

    /// <summary>
    /// Determines whether there are any more
    /// <see cref="ObservationRecord"/>s to be
    /// read from the PQDIF file.
    /// </summary>
    /// <returns>true if there is another observation record to be read from PQDIF file; false otherwise</returns>
    public boolean hasNextObservationRecord() throws IOException
    {
        Record physicalRecord;
        RecordType recordType;

        // Read records from the file until we encounter an observation record or end of file
        while (m_nextObservationRecord == null && m_physicalParser.hasNextRecord)
        {
            physicalRecord = m_physicalParser.getNextRecord();
            recordType = physicalRecord.getHeader().getTypeOfRecord();

            switch (recordType)
            {
                case DataSource:
                    // Keep track of the latest data source record in order to associate it with observation records
                    m_currentDataSourceRecord = DataSourceRecord.CreateDataSourceRecord(physicalRecord);
                    m_allDataSourceRecords.add(m_currentDataSourceRecord);
                    break;

                case MonitorSettings:
                    // Keep track of the latest monitor settings record in order to associate it with observation records
                    m_currentMonitorSettingsRecord = MonitorSettingsRecord.createMonitorSettingsRecord(physicalRecord);
                    break;

                case Observation:
                    // Found an observation record!
                    m_nextObservationRecord = ObservationRecord.createObservationRecord(physicalRecord, m_currentDataSourceRecord, m_currentMonitorSettingsRecord);
                    break;

                case Container:
                    // The container record is parsed when the file is opened; it should never be encountered here
                    throw new InvalidOperationException("Found more than one container record in PQDIF file.");

                default:
                    // Ignore unrecognized record types as the rest of the file might be valid.
                    //throw new ArgumentOutOfRangeException(string.Format("Unknown record type: {0}", physicalRecord.Header.RecordTypeTag));
                    break;
            }
        }

        return m_nextObservationRecord != null;
    }

    /// <summary>
    /// Gets the next observation record from the PQDIF file.
    /// </summary>
    /// <returns>The next observation record.</returns>
    public ObservationRecord nextObservationRecord() throws IOException
    {
        ObservationRecord nextObservationRecord;

        // Call this first to read ahead to the next
        // observation record if we haven't already
        hasNextObservationRecord();

        // We need to set m_nextObservationRecord to null so that
        // subsequent calls to HasNextObservationRecord() will
        // continue to parse new records
        nextObservationRecord = m_nextObservationRecord;
        m_nextObservationRecord = null;

        return nextObservationRecord;
    }

    /// <summary>
    /// Resets the parser to the beginning of the PQDIF file.
    /// </summary>
    public void Reset()
    {
        m_currentDataSourceRecord = null;
        m_currentMonitorSettingsRecord = null;
        m_nextObservationRecord = null;
        m_allDataSourceRecords = new ArrayList<DataSourceRecord>();

//        m_physicalParser.reset();
        try {
        	m_physicalParser.getNextRecord(); // skip container record
		} catch (Exception e) {
			// TODO: handle exception
		}
    }

    /// <summary>
    /// Closes the PQDIF file.
    /// </summary>
    public void Close()
    {
        m_physicalParser.close();
    }

    /// <summary>
    /// Releases resources held by the parser.
    /// </summary>
    public void Dispose()
    {
//        m_physicalParser.dispose();
    }

}
