package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.util.DateTime;
import com.pabu5h.pq_decoder.util.GUID;
import com.pabu5h.pq_decoder.util.GUID.GUIDSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pabu5h.pq_decoder.logical_parser.ChannelDefinition.Phase;
import com.pabu5h.pq_decoder.logical_parser.ChannelDefinition.QuantityMeasured;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.physical_parser.VectorElement;

public class DataSourceRecord {
	private Record m_physicalRecord;

    /// <summary>
    /// Creates a new instance of the <see cref="DataSourceRecord"/> class.
    /// </summary>
    /// <param name="physicalRecord">The physical structure of the data source record.</param>
    private DataSourceRecord(Record physicalRecord)
    {
        m_physicalRecord = physicalRecord;
        this.getChannelDefinitions();
        this.getDataSourceLocation();
        this.getDataSourceName();
        this.getDataSourceOwner();
        this.getDataSourceTypeID();
        this.getEffective();
        this.getEquipmentID();
        this.getLongitude();
        this.getLatitude();
        this.getVendorID();
    }

    /// <summary>
    /// Gets the physical structure of the data source record.
    /// </summary>
    public Record getPhysicalRecord()
    {
    	return m_physicalRecord;
    }

    /// <summary>
    /// Gets or sets the ID of the type of the data source.
    /// </summary>
    @JsonSerialize(using = GUIDSerializer.class)
    public GUID dataSourceTypeID;
    public GUID getDataSourceTypeID()
    {
        return dataSourceTypeID = m_physicalRecord.getBody().getCollection()
                .getScalarByTag(DataSourceTypeIDTag)
                .getGuid();
    }

	public void setDataSourceTypeID(GUID value) {
	    CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
	    ScalarElement dataSourceTypeIDElement = collectionElement.getOrAddScalar(DataSourceTypeIDTag);
	    dataSourceTypeIDElement.setTypeOfValue(PhysicalType.Guid);
	    dataSourceTypeIDElement.setGuid(value);
	}

    /// <summary>
    /// Gets or sets the ID of the vendor of the data source.
    /// </summary>
	@JsonSerialize(using = GUIDSerializer.class)
	public GUID vendorID;
    public GUID getVendorID()
    {
    	CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        ScalarElement vendorIDElement = collectionElement.getScalarByTag(VendorIDTag);

        if (vendorIDElement == null)
            return vendorID = Vendor.None;

        return vendorID = vendorIDElement.getGuid();
    }
    
    public void setVendorID(GUID value) {
        CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        ScalarElement vendorIDElement = collectionElement.getOrAddScalar(VendorIDTag);
        vendorIDElement.setTypeOfValue(PhysicalType.Guid);
        vendorIDElement.setGuid(value);
    }

    /// <summary>
    /// Gets or sets the ID of the equipment.
    /// </summary>
    @JsonSerialize(using = GUIDSerializer.class)
    public GUID equipmentID;
    public GUID getEquipmentID()
    {
        CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        ScalarElement equipmentIDElement = collectionElement.getScalarByTag(EquipmentIDTag);

        if (equipmentIDElement == null)
            return equipmentID = GUID.Empty;

        return equipmentID = equipmentIDElement.getGuid();
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement equipmentIDElement = collectionElement.getOrAddScalar(EquipmentIDTag);
//            equipmentIDElement.TypeOfValue = PhysicalType.Guid;
//            equipmentIDElement.setGuid(value);
//        }
    }
    
    public void setEquipmentID(GUID value)
    {

        CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        ScalarElement equipmentIDElement = collectionElement.getOrAddScalar(EquipmentIDTag);
        equipmentIDElement.setTypeOfValue(PhysicalType.Guid);
        equipmentIDElement.setGuid(value);
    }

    /// <summary>
    /// Gets or sets the name of the data source.
    /// </summary>
    public String dataSourceName;
    public String getDataSourceName()
    {
        CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        VectorElement dataSourceNameElement = collectionElement.getVectorByTag(DataSourceNameTag);
        return dataSourceName = new String(dataSourceNameElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
    }
    
    public void setDataSourceName(String value)
    {

        CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        byte[] bytes = ((value + (char)0)).getBytes(StandardCharsets.US_ASCII);
        collectionElement.addOrUpdateVector(DataSourceNameTag, PhysicalType.Char1, bytes);
    }

    /// <summary>
    /// Gets or sets the name of the data source Owner.
    /// </summary>
    public String dataSourceOwner;
    public String getDataSourceOwner()
    {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            VectorElement dataSourceOwnerElement = collectionElement.getVectorByTag(DataSourceOwnerTag);
            if (dataSourceOwnerElement == null)
                return null;
            return dataSourceOwner = new String(dataSourceOwnerElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            byte[] bytes = Encoding.ASCII.getBytes(value + (char)0);
//            collectionElement.AddOrUpdateVector(DataSourceOwnerTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setDataSourceOwner(String value)
    {

        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            byte[] bytes = ((value + (char)0)).getBytes(StandardCharsets.US_ASCII);
            collectionElement.addOrUpdateVector(DataSourceOwnerTag, PhysicalType.Char1, bytes);
        }
    }

    /// <summary>
    /// Gets or sets the name of the data source Location.
    /// </summary>
    public String dataSourceLocation;
    public String getDataSourceLocation()
    {
        CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        VectorElement dataSourceLocationElement = collectionElement.getVectorByTag(DataSourceLocationTag);
        if (dataSourceLocationElement == null)
            return null;
        return dataSourceLocation = new String(dataSourceLocationElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            byte[] bytes = Encoding.ASCII.getBytes(value + (char)0);
//            collectionElement.AddOrUpdateVector(DataSourceLocationTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setDataSourceLocation(String value)
    {

            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            byte[] bytes = ((value + (char)0)).getBytes(StandardCharsets.US_ASCII);
            collectionElement.addOrUpdateVector(DataSourceLocationTag, PhysicalType.Char1, bytes);
    }

    /// <summary>
    /// Gets or sets the longitude at which the data source is located.
    /// </summary>
    public double longitude;
    public double getLongitude()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            VectorElement dataSourceCoordinatesElement = collectionElement.getVectorByTag(DataSourceCoordinatesTag);

            if (dataSourceCoordinatesElement == null)
                return longitude = /*Integer.MAX_VALUE*/4294967295d;

            return longitude = dataSourceCoordinatesElement.getUInt4(0);
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            VectorElement dataSourceCoordinatesElement = collectionElement.getOrAddVector(DataSourceCoordinatesTag);
//            dataSourceCoordinatesElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            dataSourceCoordinatesElement.Size = 2;
//            dataSourceCoordinatesElement.SetUInt4(0, value);
//        }
    }
    
    public void setLongitude(int value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            VectorElement dataSourceCoordinatesElement = collectionElement.getVectorByTag(DataSourceCoordinatesTag);
//
//            if (dataSourceCoordinatesElement == null)
//                return uint.MaxValue;
//
//            return dataSourceCoordinatesElement.getUInt4(0);
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            VectorElement dataSourceCoordinatesElement = collectionElement.getOrAddVector(DataSourceCoordinatesTag);
            dataSourceCoordinatesElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            dataSourceCoordinatesElement.setSize(2);
            dataSourceCoordinatesElement.setUInt4(0, value);
//        }
    }

    /// <summary>
    /// Gets or sets the latitude at which the device is located.
    /// </summary>
    public double latitude;
    public double getLatitude()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            VectorElement dataSourceCoordinatesElement = collectionElement.getVectorByTag(DataSourceCoordinatesTag);

            if (dataSourceCoordinatesElement == null)
                return latitude = /*Integer.MAX_VALUE*/4294967295d;

            return latitude = dataSourceCoordinatesElement.getUInt4(1);
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            VectorElement dataSourceCoordinatesElement = collectionElement.getOrAddVector(DataSourceCoordinatesTag);
//            dataSourceCoordinatesElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            dataSourceCoordinatesElement.Size = 2;
//            dataSourceCoordinatesElement.SetUInt4(1, value);
//        }
    }
    
    public void setLatitude(int  value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            VectorElement dataSourceCoordinatesElement = collectionElement.getVectorByTag(DataSourceCoordinatesTag);
//
//            if (dataSourceCoordinatesElement == null)
//                return uint.MaxValue;
//
//            return dataSourceCoordinatesElement.getUInt4(1);
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            VectorElement dataSourceCoordinatesElement = collectionElement.getOrAddVector(DataSourceCoordinatesTag);
            dataSourceCoordinatesElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            dataSourceCoordinatesElement.setSize(2);
            dataSourceCoordinatesElement.setUInt4(1, value);
//        }
    }

    /// <summary>
    /// Gets the definitions for the channels defined in the data source.
    /// </summary>
    
    public List<ChannelDefinition> channelDefinitions;
    public List<ChannelDefinition> getChannelDefinitions()
    {
        return channelDefinitions = m_physicalRecord.getBody().getCollection()
                .getCollectionByTag(ChannelDefinitionsTag)
                .getElementsByTag(OneChannelDefinitionTag)
                //.Cast<CollectionElement>()
                .stream()
                //.Select(collection => new ChannelDefinition(collection, this))
                .map(collection -> new ChannelDefinition((CollectionElement) collection, this))
                .collect(Collectors.toList());
    }

    /// <summary>
    /// Gets or sets the time that this data source record became effective.
    /// </summary>
    
    public DateTime effective;
    public DateTime getEffective()
    {
    	return effective = m_physicalRecord.getBody().getCollection()
                .getScalarByTag(EffectiveTag)
                .getTimestamp();
    }
    
    public void setEffective(DateTime value)
    {
        CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
        ScalarElement effectiveElement = collectionElement.getOrAddScalar(EffectiveTag);
        effectiveElement.setTypeOfValue(PhysicalType.Timestamp);
        effectiveElement.setTimestamp(value);
    }    

    /// <summary>
    /// Adds a new channel definition to the collection
    /// of channel definitions in this data source record.
    /// </summary>
    public ChannelDefinition addNewChannelDefinition()
    {
        CollectionElement channelDefinitionElement = new CollectionElement(OneChannelDefinitionTag);
        ChannelDefinition channelDefinition = new ChannelDefinition(channelDefinitionElement, this);

        channelDefinition.setPhase(Phase.None.value);
        channelDefinition.setQuantityMeasured(QuantityMeasured.None.value);
        channelDefinitionElement.addElement(new CollectionElement(ChannelDefinition.SeriesDefinitionsTag));

        CollectionElement channelDefinitionsElement = m_physicalRecord.getBody().getCollection().getCollectionByTag(ChannelDefinitionsTag);

        if (channelDefinitionsElement == null)
        {
            channelDefinitionsElement = new CollectionElement(ChannelDefinitionsTag);
            m_physicalRecord.getBody().getCollection().addElement(channelDefinitionsElement);
        }

        channelDefinitionsElement.addElement(channelDefinitionElement);

        return channelDefinition;
    }

    /// <summary>
    /// Removes the given channel definition from the collection of channel definitions.
    /// </summary>
    /// <param name="channelDefinition">The channel definition to be removed.</param>
    public void remove(ChannelDefinition channelDefinition)
    {
        CollectionElement channelDefinitionsElement = m_physicalRecord.getBody().getCollection().getCollectionByTag(ChannelDefinitionsTag);
        List<CollectionElement> channelDefinitionElements;
        ChannelDefinition definition;

        if (channelDefinitionsElement == null)
            return;

        channelDefinitionElements = channelDefinitionsElement.getElementsByTag(OneChannelDefinitionTag)
        		.stream()
        		.map(a -> (CollectionElement) a)
        		//.Cast<CollectionElement>()
        		//.ToList()
        		.collect(Collectors.toList());
        		;

        for (CollectionElement channelDefinitionElement : channelDefinitionElements)
        {
            definition = new ChannelDefinition(channelDefinitionElement, this);

            // if (Equals(channelDefinition, definition))
            if (channelDefinition.equals(definition))
                channelDefinitionsElement.removeElement(channelDefinitionElement);
        }
    }

    /// <summary>
    /// Removes the element identified by the given tag from the record.
    /// </summary>
    /// <param name="tag">The tag of the element to be removed.</param>
    public void removeElement(GUID tag)
    {
        m_physicalRecord.getBody().getCollection().removeElementsByTag(tag);
    }

    // Static Fields

    /// <summary>
    /// Tag that identifies the data source type.
    /// </summary>
    public static GUID DataSourceTypeIDTag = new GUID("b48d8581-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the vendor ID.
    /// </summary>
    public static GUID VendorIDTag = new GUID("b48d8582-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the equipment ID.
    /// </summary>
    public static GUID EquipmentIDTag = new GUID("b48d8583-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the data source name.
    /// </summary>
    public static GUID DataSourceNameTag = new GUID("b48d8587-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the data source owner.
    /// </summary>
    public static GUID DataSourceOwnerTag = new GUID("b48d8588-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the data source owner.
    /// </summary>
    public static GUID DataSourceLocationTag = new GUID("b48d8589-f5f5-11cf-9d89-0080c72e70a3");
    /// <summary>
    /// Tag that identifies the physical location of the data source.
    /// </summary>
    public static GUID DataSourceCoordinatesTag = new GUID("b48d858b-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the channel definitions collection.
    /// </summary>
    public static GUID ChannelDefinitionsTag = new GUID("b48d858d-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the a single channel definition in the collection.
    /// </summary>
    public static GUID OneChannelDefinitionTag = new GUID("b48d858e-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the time that the data source record becomes effective.
    /// </summary>
    public static GUID EffectiveTag = new GUID("62f28183-f9c4-11cf-9d89-0080c72e70a3");

    // Static Methods

    /// <summary>
    /// Creates a new data source record from scratch.
    /// </summary>
    /// <param name="dataSourceName">The name of the data source to be created.</param>
    /// <returns>The new data source record.</returns>
    public static DataSourceRecord CreateDataSourceRecord(String dataSourceName)
    {
        GUID recordTypeTag = Record.getTypeAsTag(RecordType.DataSource);
        Record physicalRecord = new Record(recordTypeTag);
        DataSourceRecord dataSourceRecord = new DataSourceRecord(physicalRecord);

//        DateTime now = DateTime.UtcNow;
        dataSourceRecord.setDataSourceTypeID(DataSourceType.Simulate);
        dataSourceRecord.setDataSourceName(dataSourceName);
        dataSourceRecord.setEffective(DateTime.utcNow());

        CollectionElement bodyElement = physicalRecord.getBody().getCollection();
        bodyElement.addElement(new CollectionElement(ChannelDefinitionsTag));

        return dataSourceRecord;
    }

    /// <summary>
    /// Creates a new data source record from the given physical record
    /// if the physical record is of type data source. Returns null if
    /// it is not.
    /// </summary>
    /// <param name="physicalRecord">The physical record used to create the data source record.</param>
    /// <returns>The new data source record, or null if the physical record does not define a data source record.</returns>
    public static DataSourceRecord CreateDataSourceRecord(Record physicalRecord)
    {
        boolean isValidDataSourceRecord = physicalRecord.getHeader().getTypeOfRecord() == RecordType.DataSource;
        return isValidDataSourceRecord ? new DataSourceRecord(physicalRecord) : null;
    }
    
    public static class DataSourceType
    {
        /// <summary>
        /// The ID for data source type Measure.
        /// </summary>
        public static GUID Measure = new GUID("e6b51730-f747-11cf-9d89-0080c72e70a3");

        /// <summary>
        /// The ID for data source type Manual.
        /// </summary>
        public static GUID Manual = new GUID("e6b51731-f747-11cf-9d89-0080c72e70a3");

        /// <summary>
        /// The ID for data source type Simulate.
        /// </summary>
        public static GUID Simulate = new GUID("e6b51732-f747-11cf-9d89-0080c72e70a3");

        /// <summary>
        /// The ID for data source type Benchmark.
        /// </summary>
        public static GUID Benchmark = new GUID("e6b51733-f747-11cf-9d89-0080c72e70a3");

        /// <summary>
        /// The ID for data source type Debug.
        /// </summary>
        public static GUID Debug = new GUID("e6b51734-f747-11cf-9d89-0080c72e70a3");

        /// <summary>
        /// Gets information about the data source type identified by the given ID.
        /// </summary>
        /// <param name="dataSourceTypeID">Globally unique identifier for the data source type.</param>
        /// <returns>The information about the data source type.</returns>
//        public static Identifier GetInfo(Guid dataSourceTypeID)
//        {
//            Identifier identifier;
//            return DataSourceTypeLookup.TryGetValue(dataSourceTypeID, out identifier) ? identifier : null;
//        }

        /// <summary>
        /// Converts the given data source type ID to a string containing the name of the data source type.
        /// </summary>
        /// <param name="dataSourceTypeID">The ID of the data source type to be converted to a string.</param>
        /// <returns>A string containing the name of the data source type with the given ID.</returns>
//        public static String toString(GUID dataSourceTypeID)
//        {
//            return GetInfo(dataSourceTypeID)?.Name ?? dataSourceTypeID.ToString();
//        }

//        private static Dictionary<Guid, Identifier> DataSourceTypeLookup
//        {
//            get
//            {
//                Tag dataSourceTypeTag = Tag.getTag(DataSourceRecord.VendorIDTag);
//
//                if (s_dataSourceTypeTag != dataSourceTypeTag)
//                {
//                    s_dataSourceTypeLookup = dataSourceTypeTag.ValidIdentifiers.ToDictionary(id => Guid.Parse(id.Value));
//                    s_dataSourceTypeTag = dataSourceTypeTag;
//                }
//
//                return s_dataSourceTypeLookup;
//            }
//        }
//
//        private static Tag s_dataSourceTypeTag;
//        private static Dictionary<Guid, Identifier> s_dataSourceTypeLookup;
    }
}
