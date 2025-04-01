package com.pabu5h.pq_decoder.logical_parser;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.physical_parser.VectorElement;
import com.pabu5h.pq_decoder.util.DateTime;
import com.pabu5h.pq_decoder.util.GUID;
import com.pabu5h.pq_decoder.util.GUID.GUIDSerializer;

@Component
public class ObservationRecord {
	
    public enum TriggerMethod
    {
        /// <summary>
        /// No trigger.
        /// </summary>
//        None = 0u,
    	None(0),

        /// <summary>
        /// A specific channel (or channels) caused the trigger; should be
        /// used with tagChannelTriggerIdx to specify which channels.
        /// </summary>
        Channel(1),

        /// <summary>
        /// Periodic data trigger.
        /// </summary>
        Periodic(2),

        /// <summary>
        /// External system trigger.
        /// </summary>
        External(3),

        /// <summary>
        /// Periodic statistical data.
        /// </summary>
        PeriodicStats(4);
    	
        final int value;

        // Constructor to set the value of the enum constant
        TriggerMethod(int value) {
            this.value = value;
        }
    	
        public static TriggerMethod from(int vl) {
        	for (TriggerMethod p : TriggerMethod.values()) {
        		if (p.value == vl) {
        			return p;
        		}
        	}
        	
        	return null;
        }
    }
	
	private Record m_physicalRecord;
    private DataSourceRecord m_dataSource;
    private MonitorSettingsRecord m_settings;

//    #endregion

//    #region [ Constructors ]

    /// <summary>
    /// Creates a new instance of the <see cref="ObservationRecord"/> class.
    /// </summary>
    /// <param name="physicalRecord">The physical structure of the observation record.</param>
    /// <param name="dataSource">The data source record that defines the channels in this observation record.</param>
    /// <param name="settings">The monitor settings to be applied to this observation record.</param>
    private ObservationRecord(Record physicalRecord, DataSourceRecord dataSource, MonitorSettingsRecord settings)
    {
        m_physicalRecord = physicalRecord;
        m_dataSource = dataSource;
        m_settings = settings;
        this.getChannelInstances();
        this.getChannelTriggerIndex();
        this.getCreateTime();
        this.getDataSource();
        this.getDisturbanceCategoryID();
        this.getName();
        this.getPhysicalRecord();
        this.getSettings();
        this.getStartTime();
        this.getTimeTriggered();
        this.getTriggerMethod();
    }

//    #endregion

//    #region [ Properties ]

    /// <summary>
    /// Gets the physical structure of the observation record.
    /// </summary>
    Record physicalRecord;
    public Record getPhysicalRecord()
    {
//        get
//        {
            return physicalRecord = m_physicalRecord;
//        }
    }

    /// <summary>
    /// Gets the data source record that defines
    /// the channels in this observation record.
    /// </summary>
    DataSourceRecord dataSource;
    public DataSourceRecord getDataSource()
    {
//        get
//        {
            return dataSource = m_dataSource;
//        }
    }

    /// <summary>
    /// Gets the monitor settings record that defines the
    /// settings to be applied to this observation record.
    /// </summary>
    MonitorSettingsRecord settings;
    public MonitorSettingsRecord getSettings()
    {
//        get
//        {
            return settings = m_settings;
//        }
    }

    /// <summary>
    /// Gets the name of the observation record.
    /// </summary>
    String name;
    public String getName()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            VectorElement nameElement = collectionElement.getVectorByTag(ObservationNameTag);
            return name = new String(nameElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            byte[] bytes = Encoding.ASCII.getBytes(value + (char)0);
//            collectionElement.addOrUpdateVector(ObservationNameTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setName(String value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            VectorElement nameElement = collectionElement.getVectorByTag(ObservationNameTag);
//            return Encoding.ASCII.getString(nameElement.getValues()).Trim((char)0);
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            byte[] bytes = (value + (char)0).getBytes(StandardCharsets.US_ASCII);
            collectionElement.addOrUpdateVector(ObservationNameTag, PhysicalType.Char1, bytes);
            getName();
//        }
    }

    /// <summary>
    /// Gets the creation time of the observation record.
    /// </summary>
    DateTime createTime;
    public DateTime getCreateTime()
    {
//        get
//        {
            return createTime = m_physicalRecord.getBody().getCollection()
                .getScalarByTag(TimeCreateTag)
                .getTimestamp();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement timeCreateElement = collectionElement.getOrAddScalar(TimeCreateTag);
//            timeCreateElement.TypeOfValue = PhysicalType.Timestamp;
//            timeCreateElement.setTimestamp(value);
//        }
    }
    
    public void setCreateTime(DateTime value)
    {
//        get
//        {
//            return m_physicalRecord.getBody().getCollection()
//                .getScalarByTag(TimeCreateTag)
//                .getTimestamp();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement timeCreateElement = collectionElement.getOrAddScalar(TimeCreateTag);
            timeCreateElement.setTypeOfValue(PhysicalType.Timestamp);
            timeCreateElement.setTimestamp(value);
            getCreateTime();
//        }
    }

    /// <summary>
    /// Gets the starting time of the data in the observation record. This time should
    /// be used as the base time for all relative seconds recorded in the series instances.
    /// </summary>
    /// <remarks>
    /// The <see cref="ObservationRecord"/> contains two timestamp fields: <see cref="StartTime"/> and
    /// <see cref="TimeTriggered"/>. The StartTime is a required part of any Observation, whereas the 
    /// <see cref="TimeTriggered"/> is optional. 
    /// 
    /// The <see cref="StartTime"/> does not have to be the same as the trigger time and can therefore 
    /// be chosen more or less arbitrarily. For instance, you can choose to record the 
    /// <see cref="StartTime"/> as the timestamp of the first data point in the observation or the top 
    /// of an interval in which the data was captured. The trigger time field is more well defined, 
    /// essentially always representing the point in time at which the data source decided to capture 
    /// some data in the PQDIF file.
    /// </remarks>
    DateTime startTime;
    public DateTime getStartTime()
    {
//        get
//        {
            return startTime = m_physicalRecord.getBody().getCollection()
                .getScalarByTag(TimeStartTag)
                .getTimestamp();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement timeStartElement = collectionElement.getOrAddScalar(TimeStartTag);
//            timeStartElement.TypeOfValue = PhysicalType.Timestamp;
//            timeStartElement.setTimestamp(value);
//        }
    }
    
    public void setStartTime(DateTime value)
    {
//        get
//        {
//            return m_physicalRecord.getBody().getCollection()
//                .getScalarByTag(TimeStartTag)
//                .getTimestamp();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement timeStartElement = collectionElement.getOrAddScalar(TimeStartTag);
            timeStartElement.setTypeOfValue(PhysicalType.Timestamp);
            timeStartElement.setTimestamp(value);
            getStartTime();
//        }
    }

    /// <summary>
    /// Gets or sets the type of trigger which caused the observation.
    /// </summary>
    TriggerMethod triggerMethod;
    public TriggerMethod getTriggerMethod()
    {
//        get
//        {
            return triggerMethod = TriggerMethod.from(m_physicalRecord.getBody().getCollection()
                .getScalarByTag(TriggerMethodTag)
                .getUInt4());
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement triggerMethodElement = collectionElement.getOrAddScalar(TriggerMethodTag);
//            triggerMethodElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            triggerMethodElement.setUInt4((uint)value);
//        }
    }
    
    public void setTriggerMethod(int value)
    {
//        get
//        {
//            return (TriggerMethod)m_physicalRecord.getBody().getCollection()
//                .getScalarByTag(TriggerMethodTag)
//                .getUInt4();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement triggerMethodElement = collectionElement.getOrAddScalar(TriggerMethodTag);
            triggerMethodElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            triggerMethodElement.setUInt4(value);
            getTriggerMethod();
//        }
    }


    /// <summary>
    /// Gets the time the observation was triggered. For more information regarding recording
    /// time in PQD files, see <see cref="StartTime"/>.
    /// </summary>
    DateTime timeTriggered;
    public DateTime getTimeTriggered()
    {
//        get
//        {
            ScalarElement timeTriggeredElement = m_physicalRecord.getBody().getCollection()
                .getScalarByTag(TimeTriggeredTag);

            if (timeTriggeredElement == null)
                return timeTriggered = DateTime.MinValue;

            return timeTriggered = timeTriggeredElement.getTimestamp();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement timeTriggeredElement = collectionElement.getOrAddScalar(TimeTriggeredTag);
//            timeTriggeredElement.TypeOfValue = PhysicalType.Timestamp;
//            timeTriggeredElement.setTimestamp(value);
//        }
    }
    
    public void setTimeTriggered(DateTime value)
    {
//        get
//        {
//            ScalarElement timeTriggeredElement = m_physicalRecord.getBody().getCollection()
//                .getScalarByTag(TimeTriggeredTag);
//
//            if (timeTriggeredElement == null)
//                return new Date(0l);
//
//            return timeTriggeredElement.getTimestamp();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement timeTriggeredElement = collectionElement.getOrAddScalar(TimeTriggeredTag);
            timeTriggeredElement.setTypeOfValue(PhysicalType.Timestamp);
            timeTriggeredElement.setTimestamp(value);
            getTimeTriggered();
//        }
    }

    /// <summary>
    /// Gets or sets the Disturbance Category ID
    /// </summary>
    @JsonSerialize(using = GUIDSerializer.class)
    GUID disturbanceCategoryID;
    public GUID getDisturbanceCategoryID()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement DisturbanceIDElement = collectionElement.getScalarByTag(DisturbanceCategoryTag);

            if (DisturbanceIDElement == null)
                return disturbanceCategoryID = DisturbanceCategory.None;

            return disturbanceCategoryID = DisturbanceIDElement.getGuid();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement vendorIDElement = collectionElement.getOrAddScalar(DisturbanceCategoryTag);
//            vendorIDElement.TypeOfValue = PhysicalType.GUID;
//            vendorIDElement.setGUID(value);
//        }
    }
    
    public void setDisturbanceCategoryID(GUID value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement DisturbanceIDElement = collectionElement.getScalarByTag(DisturbanceCategoryTag);
//
//            if ((object)DisturbanceIDElement == null)
//                return DisturbanceCategory.None;
//
//            return DisturbanceIDElement.getGUID();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement vendorIDElement = collectionElement.getOrAddScalar(DisturbanceCategoryTag);
            vendorIDElement.setTypeOfValue(PhysicalType.Guid);
            vendorIDElement.setGuid(value);
            getDisturbanceCategoryID();
//        }
    }


    /// <summary>
    /// Gets or sets the index into <see cref="ChannelInstancesTag"/> collection within this record which initiated the observation.
    /// </summary>
    public int[] getChannelTriggerIndex()
    {
//        get
//        {
            VectorElement channelTriggerIndexElement = m_physicalRecord.getBody().getCollection()
                .getVectorByTag(ChannelTriggerIndexTag);

            
            if (channelTriggerIndexElement == null)
                return new int[0];

            int[] rp = new int[channelTriggerIndexElement.getByteSize()];
            for (int i = 0; i < rp.length; i++) {
            	rp[i] = (int) channelTriggerIndexElement.getUInt4(i);
            }
            return rp;
//            return Enumerable.Range(0, channelTriggerIndexElement.Size)
//                .Select(index => channelTriggerIndexElement.getUInt4(index))
//                .ToArray();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            VectorElement channelTriggerIndexElement = collectionElement.getOrAddVector(ChannelTriggerIndexTag);
//            channelTriggerIndexElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            channelTriggerIndexElement.Size = value.Length;
//
//            for (int i = 0; i < value.Length; i++)
//                channelTriggerIndexElement.setUInt4(i, value[i]);
//        }
    }
    
    public void setChannelTriggerIndex(int[] value)
    {
//        get
//        {
//            VectorElement channelTriggerIndexElement = m_physicalRecord.getBody().getCollection()
//                .getVectorByTag(ChannelTriggerIndexTag);
//
//            if ((object)channelTriggerIndexElement == null)
//                return new uint[0];
//
//            return Enumerable.Range(0, channelTriggerIndexElement.Size)
//                .Select(index => channelTriggerIndexElement.getUInt4(index))
//                .ToArray();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            VectorElement channelTriggerIndexElement = collectionElement.getOrAddVector(ChannelTriggerIndexTag);
            channelTriggerIndexElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            channelTriggerIndexElement.setSize(value.length);

            for (int i = 0; i < value.length; i++) {
            	channelTriggerIndexElement.setUInt4(i, value[i]);
            }
                
//        }
    }

    /// <summary>
    /// Gets the channel instances in this observation record.
    /// </summary>
    List<ChannelInstance> channelInstances;
    public List<ChannelInstance> getChannelInstances()
    {
//        get
//        {
            return channelInstances = m_physicalRecord.getBody().getCollection()
                .getCollectionByTag(ChannelInstancesTag)
                .getElementsByTag(OneChannelInstanceTag)
//                .Cast<CollectionElement>()
                .stream()
                .map(collection -> new ChannelInstance((CollectionElement) collection, this))
//                .Select(collection => new ChannelInstance(collection, this))
                .collect(Collectors.toList());
//        }
    }

//    #endregion

//    #region [ Methods ]

    /// <summary>
    /// Adds a new channel instance to the collection
    /// of channel instances in this observation record.
    /// </summary>
    public ChannelInstance addNewChannelInstance(ChannelDefinition channelDefinition)
    {
        CollectionElement channelInstanceElement = new CollectionElement(OneChannelInstanceTag);// { TagOfElement = OneChannelInstanceTag };
        ChannelInstance channelInstance = new ChannelInstance(channelInstanceElement, this);

        channelInstance.setChannelDefinitionIndex(channelDefinition.getDataSource().getChannelDefinitions().indexOf(channelDefinition));
        channelInstanceElement.addElement(new CollectionElement(ChannelInstance.SeriesInstancesTag));

        CollectionElement channelInstancesElement = m_physicalRecord.getBody().getCollection().getCollectionByTag(ChannelInstancesTag);

        if (channelInstancesElement == null)
        {
            channelInstancesElement = new CollectionElement(ChannelInstancesTag);// { TagOfElement = ChannelInstancesTag };
            m_physicalRecord.getBody().getCollection().addElement(channelInstancesElement);
        }

        channelInstancesElement.addElement(channelInstanceElement);

        return channelInstance;
    }

    /// <summary>
    /// Removes the given channel instance from the collection of channel instances.
    /// </summary>
    /// <param name="channelInstance">The channel instance to be removed.</param>
    public void Remove(ChannelInstance channelInstance)
    {
        CollectionElement channelInstancesElement = m_physicalRecord.getBody().getCollection().getCollectionByTag(ChannelInstancesTag);
        List<CollectionElement> channelInstanceElements;
        ChannelInstance instance;

        if (channelInstancesElement == null)
            return;

        channelInstanceElements = channelInstancesElement.getElementsByTag(OneChannelInstanceTag)
        		.stream()
        		.map(a -> (CollectionElement) a)
//        		.Cast<CollectionElement>()
        		.collect(Collectors.toList());

        for (CollectionElement channelSettingElement : channelInstanceElements)
        {
            instance = new ChannelInstance(channelSettingElement, this);

            if (channelInstance.equals(instance))
                channelInstancesElement.removeElement(channelSettingElement);
        }
    }

//    #endregion

//    #region [ Static ]

    // Static Fields

    /// <summary>
    /// Tag that identifies the name of the observation record.
    /// </summary>
    public static GUID ObservationNameTag = new GUID("3d786f8a-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the time that the observation record was created.
    /// </summary>
    public static GUID TimeCreateTag = new GUID("3d786f8b-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the start time of the data in the observation record.
    /// </summary>
    public static GUID TimeStartTag = new GUID("3d786f8c-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the type of trigger that caused the observation.
    /// </summary>
    public static GUID TriggerMethodTag = new GUID("3d786f8d-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the time the observation was triggered.
    /// </summary>
    public static GUID TimeTriggeredTag = new GUID("3d786f8e-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the index into <see cref="ChannelInstancesTag"/> collection within this record. This specifies which channel(s) initiated the observation.
    /// </summary>
    public static GUID ChannelTriggerIndexTag = new GUID("3d786f8f-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the channel instances collection.
    /// </summary>
    public static GUID ChannelInstancesTag = new GUID("3d786f91-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies a single channel instance in the collection.
    /// </summary>
    public static GUID OneChannelInstanceTag = new GUID("3d786f92-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the Disturbance Category.
    /// </summary>
    public static GUID DisturbanceCategoryTag = new GUID("b48d8597-f5f5-11cf-9d89-0080c72e70a3");


    // Static Methods

    /// <summary>
    /// Creates a new observation record from scratch with the given data source and settings.
    /// </summary>
    /// <param name="dataSource">The data source record that defines the channels in this observation record.</param>
    /// <param name="settings">The monitor settings to be applied to this observation record.</param>
    /// <returns>The new observation record.</returns>
    public static ObservationRecord createObservationRecord(DataSourceRecord dataSource, MonitorSettingsRecord settings)
    {
        GUID recordTypeTag = Record.getTypeAsTag(RecordType.Observation);
        Record physicalRecord = new Record(recordTypeTag);
        ObservationRecord observationRecord = new ObservationRecord(physicalRecord, dataSource, settings);

        DateTime now = DateTime.utcNow();
        observationRecord.setName(now.toString());
        observationRecord.setCreateTime(now);
        observationRecord.setStartTime(now);
        observationRecord.setTriggerMethod(TriggerMethod.None.value);

        CollectionElement bodyElement = physicalRecord.getBody().getCollection();
        bodyElement.addElement(new CollectionElement(ChannelInstancesTag));

        return observationRecord;
    }

    /// <summary>
    /// Creates a new observation record from the given physical record
    /// if the physical record is of type observation. Returns null if
    /// it is not.
    /// </summary>
    /// <param name="physicalRecord">The physical record used to create the observation record.</param>
    /// <param name="dataSource">The data source record that defines the channels in this observation record.</param>
    /// <param name="settings">The monitor settings to be applied to this observation record.</param>
    /// <returns>The new observation record, or null if the physical record does not define a observation record.</returns>
    public static ObservationRecord createObservationRecord(Record physicalRecord, DataSourceRecord dataSource, MonitorSettingsRecord settings)
    {
        boolean isValidObservationRecord = physicalRecord.getHeader().getTypeOfRecord() == RecordType.Observation;
        return isValidObservationRecord ? new ObservationRecord(physicalRecord, dataSource, settings) : null;
    }

//    #endregion
}
