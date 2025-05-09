package com.pabu5h.pq_decoder.logical_parser;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pabu5h.pq_decoder.logical_parser.SeriesDefinition.QuantityUnits;
import com.pabu5h.pq_decoder.logical_parser.SeriesDefinition.StorageMethods;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.physical_parser.VectorElement;
import com.pabu5h.pq_decoder.util.GUID;
import com.pabu5h.pq_decoder.util.GUID.GUIDSerializer;

public class ChannelDefinition {

	public enum Phase
    {
        /// <summary>
        /// Phase is not applicable.
        /// </summary>
        None(0),

        /// <summary>
        /// A-to-neutral.
        /// </summary>
        AN(1),

        /// <summary>
        /// B-to-neutral.
        /// </summary>
        BN(2),

        /// <summary>
        /// C-to-neutral.
        /// </summary>
        CN(3),

        /// <summary>
        /// Neutral-to-ground.
        /// </summary>
        NG(4),

        /// <summary>
        /// A-to-B.
        /// </summary>
        AB(5),

        /// <summary>
        /// B-to-C.
        /// </summary>
        BC(6),

        /// <summary>
        /// C-to-A.
        /// </summary>
        CA(7),

        /// <summary>
        /// Residual - the vector or point-on-wave sum of Phases A, B, and C.
        /// Should be zero in a perfectly balanced system.
        /// </summary>
        Residual(8),

        /// <summary>
        /// Net - the vector or point-on-wave sum of Phases A, B, C and the
        /// Neutral phase. Should be zero in a 4-wire system with no earth
        /// return path.
        /// </summary>
        Net(9),

        /// <summary>
        /// Positive sequence.
        /// </summary>
        PositiveSequence(10),

        /// <summary>
        /// Negative sequence.
        /// </summary>
        NegativeSequence(11),

        /// <summary>
        /// Zero sequence.
        /// </summary>
        ZeroSequence(12),

        /// <summary>
        /// The value representing a total or other
        /// summarizing value in a multi-phase system.
        /// </summary>
        Total(13),

        /// <summary>
        /// The value representing average of 3 line-neutral values.
        /// </summary>
        LineToNeutralAverage(14),

        /// <summary>
        /// The value representing average of 3 line-line values.
        /// </summary>
        LineToLineAverage(15),

        /// <summary>
        /// The value representing the "worst" of the 3 phases.
        /// </summary>
        Worst(16),

        /// <summary>
        /// DC Positive.
        /// </summary>
        Plus(17),

        /// <summary>
        /// DC Negative.
        /// </summary>
        Minus(18),

        /// <summary>
        /// Generic Phase 1.
        /// </summary>
        General1(19),

        /// <summary>
        /// Generic Phase 2.
        /// </summary>
        General2(20),

        /// <summary>
        /// Generic Phase 3.
        /// </summary>
        General3(21),

        /// <summary>
        /// Generic Phase 4.
        /// </summary>
        General4(22),

        /// <summary>
        /// Generic Phase 5.
        /// </summary>
        General5(23),

        /// <summary>
        /// Generic Phase 6.
        /// </summary>
        General6(24),

        /// <summary>
        /// Generic Phase 7.
        /// </summary>
        General7(25),

        /// <summary>
        /// Generic Phase 8.
        /// </summary>
        General8(26),

        /// <summary>
        /// Generic Phase 9.
        /// </summary>
        General9(27),

        /// <summary>
        /// Generic Phase 10.
        /// </summary>
        General10(28),

        /// <summary>
        /// Generic Phase 11.
        /// </summary>
        General11(29),

        /// <summary>
        /// Generic Phase 12.
        /// </summary>
        General12(30),

        /// <summary>
        /// Generic Phase 13.
        /// </summary>
        General13(31),

        /// <summary>
        /// Generic Phase 14.
        /// </summary>
        General14(32),

        /// <summary>
        /// Generic Phase 15.
        /// </summary>
        General15(33),

        /// <summary>
        /// Generic Phase 16.
        /// </summary>
        General16(34);
		
        final int value;

        // Constructor to set the value of the enum constant
        Phase(int value) {
            this.value = value;
        }
        
        public static Phase from(int vl) {
        	for (Phase p : Phase.values()) {
        		if (p.value == vl) {
        			return p;
        		}
        	}
        	
        	return null;
        }
    }

    /// <summary>
    /// Physical quantity under measurement.
    /// </summary>
    public enum QuantityMeasured
    {
        /// <summary>
        /// None or not applicable.
        /// </summary>
        None(0),

        /// <summary>
        /// Voltage.
        /// </summary>
        Voltage(1),

        /// <summary>
        /// Current.
        /// </summary>
        Current(2),

        /// <summary>
        /// Power - includes all data for a quantity or characteristic
        /// derived from multiplying voltage and current components.
        /// </summary>
        Power(3),

        /// <summary>
        /// Energy - includes all data from an integration of a quantity
        /// or characteristic derived from multiplying voltage and current
        /// components together.
        /// </summary>
        Energy(4),

        /// <summary>
        /// Temperature.
        /// </summary>
        Temperature(5),

        /// <summary>
        /// Pressure.
        /// </summary>
        Pressure(6),

        /// <summary>
        /// Charge.
        /// </summary>
        Charge(7),

        /// <summary>
        /// Electrical field.
        /// </summary>
        ElectricalField(8),

        /// <summary>
        /// Magnetic field.
        /// </summary>
        MagneticField(9),

        /// <summary>
        /// Velocity.
        /// </summary>
        Velocity(10),

        /// <summary>
        /// Compass bearing.
        /// </summary>
        Bearing(11),

        /// <summary>
        /// Applied force, electrical, mechanical etc.
        /// </summary>
        Force(12),

        /// <summary>
        /// Torque.
        /// </summary>
        Torque(13),

        /// <summary>
        /// Spatial position.
        /// </summary>
        Position(14),

        /// <summary>
        /// Flux linkage Weber Turns.
        /// </summary>
        FluxLinkage(15),

        /// <summary>
        /// Magnetic field density.
        /// </summary>
        FluxDensity(16),

        /// <summary>
        /// Status data.
        /// </summary>
        Status(17);
        
        final int value;

        // Constructor to set the value of the enum constant
    	QuantityMeasured(int value) {
            this.value = value;
        }
    	
        public static QuantityMeasured from(int vl) {
        	for (QuantityMeasured p : QuantityMeasured.values()) {
        		if (p.value == vl) {
        			return p;
        		}
        	}
        	
        	return null;
        }
    }
    
    private CollectionElement m_physicalStructure;
    private DataSourceRecord m_dataSource;

    /// <summary>
    /// Creates a new instance of the <see cref="ChannelDefinition"/> class.
    /// </summary>
    /// <param name="physicalStructure">The collection element which is the physical structure of the channel definition.</param>
    /// <param name="dataSource">The data source in which the channel definition resides.</param>
    public ChannelDefinition(CollectionElement physicalStructure, DataSourceRecord dataSource)
    {
        m_dataSource = dataSource;
        m_physicalStructure = physicalStructure;
        this.getChannelName();
        this.getDataSource();
        this.getPhase();
        this.getPhysicalStructure();
        this.getQuantityMeasured();
        this.getQuantityName();
        this.getQuantityTypeID();
        this.getSeriesDefinitions();
    }

    /// <summary>
    /// Gets the physical structure of the channel definition.
    /// </summary>
    public CollectionElement getPhysicalStructure()
    {
            return m_physicalStructure;
    }

    /// <summary>
    /// Gets the data source record in which
    /// the channel definition resides.
    /// </summary>
    
    @JsonIgnore
    public DataSourceRecord getDataSource()
    {
            return m_dataSource;
    }

    /// <summary>
    /// Gets a String identifier for the channel definition.
    /// </summary>
    String channelName;
    public String getChannelName()
    {
            VectorElement channelNameElement = m_physicalStructure.getVectorByTag(ChannelNameTag);

            if (channelNameElement == null)
                return channelName = null;

            return channelName = new String(channelNameElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        set
//        {
//            byte[] bytes = Encoding.ASCII.GetBytes(value + (char)0);
//            m_physicalStructure.addOrUpdateVector(ChannelNameTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setChannelName(String value)
    {
    	byte[] bytes = (value + "0").getBytes(StandardCharsets.US_ASCII);
        m_physicalStructure.addOrUpdateVector(ChannelNameTag, PhysicalType.Char1, bytes);
        getChannelName();
    }    

    /// <summary>
    /// Gets the phase measured by the device.
    /// </summary>
    Phase phase;
    public Phase getPhase()
    {
        return phase = Phase.from(
        		m_physicalStructure
                .getScalarByTag(PhaseIDTag)
                .getUInt4()
        		);
        
//        set
//        {
//            ScalarElement phaseIDElement = m_physicalStructure.getOrAddScalar(PhaseIDTag);
//            phaseIDElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            phaseIDElement.setUInt4((uint)value);
//        }
    }
    
    public void setPhase(int value)
    {

        {
            ScalarElement phaseIDElement = m_physicalStructure.getOrAddScalar(PhaseIDTag);
            phaseIDElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            phaseIDElement.setUInt4(value);
            getPhase();
        }
    }

    /// <summary>
    /// Gets the quantity type ID, which specifies how the data
    /// inside instances of this definition should be interpreted.
    /// </summary>
    @JsonSerialize(using = GUIDSerializer.class)
    GUID quantityTypeID;
    public GUID getQuantityTypeID()
    {
        return quantityTypeID = m_physicalStructure
                .getScalarByTag(QuantityTypeIDTag)
                .getGuid();
//        set
//        {
//            ScalarElement quantityTypeIDElement = m_physicalStructure.getOrAddScalar(QuantityTypeIDTag);
//            quantityTypeIDElement.TypeOfValue = PhysicalType.Guid;
//            quantityTypeIDElement.setGuid(value);
//        }
    }
    
    public void setQuantityTypeID(GUID value)
    {

            ScalarElement quantityTypeIDElement = m_physicalStructure.getOrAddScalar(QuantityTypeIDTag);
            quantityTypeIDElement.setTypeOfValue(PhysicalType.Guid);
            quantityTypeIDElement.setGuid(value);
            getQuantityTypeID();
    }

    /// <summary>
    /// Gets the physical quantity under measurement
    /// (Voltage, Current Power, etc).
    /// </summary>
    QuantityMeasured quantityMeasured;
    public QuantityMeasured getQuantityMeasured()
    {
            return quantityMeasured = QuantityMeasured.from(
            		m_physicalStructure
	                .getScalarByTag(QuantityMeasuredIDTag)
	                .getUInt4()
                );
//        set
//        {
//            ScalarElement quantityMeasuredIDElement = m_physicalStructure.getOrAddScalar(QuantityMeasuredIDTag);
//            quantityMeasuredIDElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            quantityMeasuredIDElement.setUInt4((uint)value);
//        }
    }
    
    public void setQuantityMeasured(int value)
    {
        ScalarElement quantityMeasuredIDElement = m_physicalStructure.getOrAddScalar(QuantityMeasuredIDTag);
        quantityMeasuredIDElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
        quantityMeasuredIDElement.setUInt4(value);
        getQuantityMeasured();
    }

    /// <summary>
    /// Gets the name of the quantity.
    /// </summary>
    String quantityName;
    public String getQuantityName()
    {

        VectorElement quantityNameElement = m_physicalStructure.getVectorByTag(QuantityNameTag);

        if (quantityNameElement == null)
            return quantityName = null;

        return quantityName = new String(quantityNameElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        set
//        {
//            byte[] bytes = Encoding.ASCII.GetBytes(value + (char)0);
//            m_physicalStructure.addOrUpdateVector(QuantityNameTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setQuantityName(String value)
    {
        byte[] bytes = (value + (char)0).getBytes(StandardCharsets.US_ASCII);
        m_physicalStructure.addOrUpdateVector(QuantityNameTag, PhysicalType.Char1, bytes);
        getQuantityName();
    }
      

    /// <summary>
    /// Gets the series definitions defined in this channel definition.
    /// </summary>
    @JsonIgnore
    List<SeriesDefinition> seriesDefinitions;
    public List<SeriesDefinition> getSeriesDefinitions()
    {
        return seriesDefinitions = m_physicalStructure.getCollectionByTag(SeriesDefinitionsTag)
                .getElementsByTag(OneSeriesDefinitionTag)
                .stream()
                .map(collection -> new SeriesDefinition((CollectionElement) collection, this))
                .collect(Collectors.toList());
    }

    /// <summary>
    /// Adds a new series definition to the collection
    /// of series definitions in this channel definition.
    /// </summary>
    public SeriesDefinition addNewSeriesDefinition()
    {
        CollectionElement seriesDefinitionElement = new CollectionElement(OneSeriesDefinitionTag);
        SeriesDefinition seriesDefinition = new SeriesDefinition(seriesDefinitionElement, this);

        seriesDefinition.setValueTypeID(SeriesValueType.Val);
        seriesDefinition.setQuantityUnits(QuantityUnits.None.value);
        seriesDefinition.setQuantityCharacteristicID(QuantityCharacteristic.None);
        seriesDefinition.setStorageMethodID(StorageMethods.Values.value);

        CollectionElement seriesDefinitionsElement = m_physicalStructure.getCollectionByTag(SeriesDefinitionsTag);

        if (seriesDefinitionsElement == null)
        {
            seriesDefinitionsElement = new CollectionElement(SeriesDefinitionsTag);;
            m_physicalStructure.addElement(seriesDefinitionsElement);
        }

        seriesDefinitionsElement.addElement(seriesDefinitionElement);

        return seriesDefinition;
    }

    /// <summary>
    /// Removes the given series definition from the collection of series definitions.
    /// </summary>
    /// <param name="seriesDefinition">The series definition to be removed.</param>
    public void Remove(SeriesDefinition seriesDefinition)
    {
        CollectionElement seriesDefinitionsElement = m_physicalStructure.getCollectionByTag(SeriesDefinitionsTag);
        List<CollectionElement> seriesDefinitionElements;
        SeriesDefinition definition;

        if (seriesDefinitionsElement == null) {
            return;
        }

        seriesDefinitionElements = seriesDefinitionsElement.getElementsByTag(OneSeriesDefinitionTag)
        		.stream()
        		.map(a -> (CollectionElement) a)
        		.collect(Collectors.toList());

        for (CollectionElement seriesDefinitionElement : seriesDefinitionElements)
        {
            definition = new SeriesDefinition(seriesDefinitionElement, this);

            if (seriesDefinition.equals(definition)) {
            	seriesDefinitionsElement.removeElement(seriesDefinitionElement);
            }
        }
    }

    /// <summary>
    /// Indicates whether the current object is equal to another object of the same type.
    /// </summary>
    /// <param name="other">An object to compare with this object.</param>
    /// <returns>true if the current object is equal to the <paramref name="other"/> parameter; otherwise, false.</returns>
    public boolean Equals(ChannelDefinition other)
    {
        if (other == null)
            return false;

        return m_physicalStructure == other.m_physicalStructure;
    }

    /// <summary>
    /// Determines whether the specified <see cref="Object"/> is equal to the current <see cref="ChannelDefinition"/>.
    /// </summary>
    /// <param name="obj">The object to compare with the current object. </param>
    /// <returns>true if the specified object  is equal to the current object; otherwise, false.</returns>
    /// <filterpriority>2</filterpriority>
    public boolean Equals(Object obj)
    {
        return Equals((ChannelDefinition) obj);
    }

    /// <summary>
    /// Serves as a hash function for a particular type. 
    /// </summary>
    /// <returns>A hash code for the current <see cref="ChannelDefinition"/>.</returns>
    /// <filterpriority>2</filterpriority>
    public int getHashCode()
    {
        return m_physicalStructure.hashCode();
    }

    // Static Fields

    /// <summary>
    /// Tag that identifies the channel definition index.
    /// </summary>
    public static GUID ChannelDefinitionIndexTag = new GUID("b48d858f-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the channel name.
    /// </summary>
    public static GUID ChannelNameTag = new GUID("b48d8590-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the phase ID.
    /// </summary>
    public static GUID PhaseIDTag = new GUID("b48d8591-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the quantity type.
    /// </summary>
    public static GUID QuantityTypeIDTag = new GUID("b48d8592-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the quantity measured ID.
    /// </summary>
    public static GUID QuantityMeasuredIDTag = new GUID("c690e872-f755-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the quantity name.
    /// </summary>
    public static GUID QuantityNameTag = new GUID("b48d8595-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the series definitions collection.
    /// </summary>
    public static GUID SeriesDefinitionsTag = new GUID("b48d8598-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies a single series definition within the collection.
    /// </summary>
    public static GUID OneSeriesDefinitionTag = new GUID("b48d859a-f5f5-11cf-9d89-0080c72e70a3");

    // Static Methods

    /// <summary>
    /// Creates a new channel definition belonging to the given data source record.
    /// </summary>
    /// <param name="dataSourceRecord">The data source record that the new channel definition belongs to.</param>
    /// <returns>The new channel definition.</returns>
    public static ChannelDefinition CreateChannelDefinition(DataSourceRecord dataSourceRecord)
    {
        ChannelDefinition channelDefinition = dataSourceRecord.addNewChannelDefinition();
        channelDefinition.setPhase(Phase.None.value);
        channelDefinition.setQuantityMeasured(QuantityMeasured.None.value);

        CollectionElement physicalStructure = channelDefinition.getPhysicalStructure();
        physicalStructure.addElement(new CollectionElement(SeriesDefinitionsTag));

        return channelDefinition;
    }

	@Override
	public String toString() {
		return "ChannelDefinition [\nchannelName=" + channelName + ", \nphase=" + phase + ", \nquantityTypeID="
				+ quantityTypeID + ", \nquantityMeasured=" + quantityMeasured + ", \nquantityName=" + quantityName
				+ ", \nseriesDefinitions=" + seriesDefinitions + "\n]";
	}

    
}
