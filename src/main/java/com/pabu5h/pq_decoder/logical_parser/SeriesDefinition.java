package com.pabu5h.pq_decoder.logical_parser;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.physical_parser.VectorElement;
import com.pabu5h.pq_decoder.util.GUID;
import com.pabu5h.pq_decoder.util.GUID.GUIDSerializer;

public class SeriesDefinition {

    public enum StorageMethods
    {
        /// <summary>
        /// Straight array of data points.
        /// </summary>
        //Values = (uint)Bits.Bit00,
    	Values(0),
        
        /// <summary>
        /// Data values are scaled.
        /// </summary>
        //Scaled = (uint)Bits.Bit01,
    	Scaled(1),

        /// <summary>
        /// Start, count, and increment are stored and
        /// the series is recreated from those values.
        /// </summary>
        //Increment = (uint)Bits.Bit02
    	Increment(2);
    	
    	final int value;
        // Constructor to set the value of the enum constant
    	StorageMethods(int value) {
            this.value = value;
        }
        
        public static StorageMethods from(int vl) {
        	for (StorageMethods p : StorageMethods.values()) {
        		if (p.value == vl) {
        			return p;
        		}
        	}
        	
        	return null;
        }
    }

    /// <summary>
    /// Units of data defined in a PQDIF file.
    /// </summary>
    public enum QuantityUnits
    {
        /// <summary>
        /// Unitless.
        /// </summary>
//        None = 0u,
		None(0),

        /// <summary>
        /// Absolute time. Each timestamp in the series must be in absolute
        /// time using the <see cref="PhysicalType.Timestamp"/> type.
        /// </summary>
//		Timestamp = 1u,
//        Timestamp = 1u,
        Timestamp(1),

        /// <summary>
        /// Seconds relative to the start time of an observation.
        /// </summary>
        /// <seealso cref="ObservationRecord.StartTime"/>
//        Seconds = 2u,
		Seconds(2),

        /// <summary>
        /// Cycles relative to the start time of an observation.
        /// </summary>
        /// <seealso cref="ObservationRecord.StartTime"/>
//        Cycles = 3u,
		Cycles(3),

        /// <summary>
        /// Volts.
        /// </summary>
//        Volts = 6u,
		Volts(6),

        /// <summary>
        /// Amperes.
        /// </summary>
//        Amps = 7u,
		Amps(7),

        /// <summary>
        /// Volt-amperes.
        /// </summary>
//        VoltAmps = 8u,
		VoltAmps(8),

        /// <summary>
        /// Watts.
        /// </summary>
//        Watts = 9u,
		Watts(9),

        /// <summary>
        /// Volt-amperes reactive.
        /// </summary>
//        Vars = 10u,
		Vars(10),
		
        /// <summary>
        /// Ohms.
        /// </summary>
//        Ohms = 11u,
		Ohms(11),	

        /// <summary>
        /// Siemens.
        /// </summary>
//        Siemens = 12u,
		Siemens(12),

        /// <summary>
        /// Volts per ampere.
        /// </summary>
//        VoltsPerAmp = 13u,
		VoltsPerAmp(13),

        /// <summary>
        /// Joules.
        /// </summary>
//        Joules = 14u,
		Joules(14),

        /// <summary>
        /// Hertz.
        /// </summary>
//        Hertz = 15u,
		Hertz(15),

        /// <summary>
        /// Celcius.
        /// </summary>
//        Celcius = 16u,
		Celcius(16),

        /// <summary>
        /// Degrees of arc.
        /// </summary>
//        Degrees = 17u,
		Degrees(17),

        /// <summary>
        /// Decibels.
        /// </summary>
        Decibels(18),

        /// <summary>
        /// Percent.
        /// </summary>
        Percent(19),

        /// <summary>
        /// Per-unit.
        /// </summary>
        PerUnit(20),

        /// <summary>
        /// Number of counts or samples.
        /// </summary>
        Samples(21),

        /// <summary>
        /// Energy in var-hours.
        /// </summary>
        VarHours(22),

        /// <summary>
        /// Energy in watt-hours.
        /// </summary>
        WattHours(23),

        /// <summary>
        /// Energy in VA-hours.
        /// </summary>
        VoltAmpHours(24),

        /// <summary>
        /// Meters/second.
        /// </summary>
        MetersPerSecond(25),

        /// <summary>
        /// Miles/hour.
        /// </summary>
        MilesPerHour(26),

        /// <summary>
        /// Pressure in bars.
        /// </summary>
        Bars(27),

        /// <summary>
        /// Pressure in pascals.
        /// </summary>
        Pascals(28),

        /// <summary>
        /// Force in newtons.
        /// </summary>
        Newtons(29),

        /// <summary>
        /// Torque in newton-meters.
        /// </summary>
        NewtonMeters(30),

        /// <summary>
        /// Revolutions/minute.
        /// </summary>
        RevolutionsPerMinute(31),

        /// <summary>
        /// Radians/second.
        /// </summary>
        RadiansPerSecond(32),

        /// <summary>
        /// Meters.
        /// </summary>
        Meters(33),

        /// <summary>
        /// Flux linkage in Weber Turns.
        /// </summary>
        WeberTurns(34),

        /// <summary>
        /// Flux density in teslas.
        /// </summary>
        Teslas(35),

        /// <summary>
        /// Magnetic field in webers.
        /// </summary>
        Webers(36),

        /// <summary>
        /// Volts/volt transfer function.
        /// </summary>
        VoltsPerVolt(37),

        /// <summary>
        /// Amps/amp transfer function.
        /// </summary>
        AmpsPerAmp(38),
        
        /// <summary>
        /// Impedance transfer function.
        /// </summary>
        AmpsPerVolt(39);
		
    	final int value;
        // Constructor to set the value of the enum constant
    	QuantityUnits(int value) {
            this.value = value;
        }
        
        public static QuantityUnits from(int vl) {
        	for (QuantityUnits p : QuantityUnits.values()) {
        		if (p.value == vl) {
        			return p;
        		}
        	}
        	
        	return null;
        }
        
    }
    
    private CollectionElement m_physicalStructure;
    private ChannelDefinition m_channelDefinition;

    /// <summary>
    /// Creates a new instance of the <see cref="SeriesDefinition"/> class.
    /// </summary>
    /// <param name="physicalStructure">The collection that is the physical structure of the series definition.</param>
    /// <param name="channelDefinition">The channel definition in which the series definition resides.</param>
    public SeriesDefinition(CollectionElement physicalStructure, ChannelDefinition channelDefinition)
    {
        m_physicalStructure = physicalStructure;
        m_channelDefinition = channelDefinition;
        
        this.getChannelDefinition();
        this.getPhysicalStructure();
        this.getQuantityCharacteristicID();
        this.getQuantityUnits();
        this.getSeriesNominalQuantity();
        this.getStorageMethodID();
        this.getValueTypeID();
        this.getValueTypeName();
        
    }


    /// <summary>
    /// Gets the physical structure of the series definition.
    /// </summary>
    public CollectionElement getPhysicalStructure()
    {
            return m_physicalStructure;
    }
    
    /// <summary>
    /// Gets the channel definition in which the series definition resides.
    /// </summary>
    public ChannelDefinition getChannelDefinition()
    {
            return m_channelDefinition;
    }

    /// <summary>
    /// Gets the value type ID of the series.
    /// </summary>
    /// <seealso cref="SeriesValueType"/>
    
    @JsonSerialize(using = GUIDSerializer.class)
    GUID valueTypeID;
    public GUID getValueTypeID()
    {
        return valueTypeID = m_physicalStructure
                .getScalarByTag(ValueTypeIDTag)
                .getGuid();
//        set
//        {
//            ScalarElement valueTypeIDElement = m_physicalStructure.getOrAddScalar(ValueTypeIDTag);
//            valueTypeIDElement.TypeOfValue = PhysicalType.Guid;
//            valueTypeIDElement.setGuid(value);
//        }
    }
    
    public void setValueTypeID(GUID value)
    {

        ScalarElement valueTypeIDElement = m_physicalStructure.getOrAddScalar(ValueTypeIDTag);
        valueTypeIDElement.setTypeOfValue(PhysicalType.Guid);
        valueTypeIDElement.setGuid(value);
        getValueTypeID();
    }

    /// <summary>
    /// Gets the units of the data in the series.
    /// </summary>
    QuantityUnits quantityUnits;
    public QuantityUnits getQuantityUnits()
    {
        return quantityUnits = QuantityUnits.from(m_physicalStructure
                .getScalarByTag(QuantityUnitsIDTag)
                .getUInt4());
//        set
//        {
//            ScalarElement quantityUnitsIDElement = m_physicalStructure.getOrAddScalar(QuantityUnitsIDTag);
//            quantityUnitsIDElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            quantityUnitsIDElement.setUInt4((uint)value);
//        }
    }
    
    public void setQuantityUnits(int value)
    {
            ScalarElement quantityUnitsIDElement = m_physicalStructure.getOrAddScalar(QuantityUnitsIDTag);
            quantityUnitsIDElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            quantityUnitsIDElement.setUInt4(value);
            getQuantityUnits();
    }

    /// <summary>
    /// Gets additional detail about the meaning of the series data.
    /// </summary>
    
    @JsonSerialize(using = GUIDSerializer.class)
    GUID quantityCharacteristicID;
    public GUID getQuantityCharacteristicID()
    {
//        get
//        {
            return quantityCharacteristicID = m_physicalStructure
                .getScalarByTag(QuantityCharacteristicIDTag)
                .getGuid();
//        }
//        set
//        {
//            ScalarElement quantityCharacteristicIDElement = m_physicalStructure.getOrAddScalar(QuantityCharacteristicIDTag);
//            quantityCharacteristicIDElement.TypeOfValue = PhysicalType.Guid;
//            quantityCharacteristicIDElement.setGuid(value);
//        }
    }
    
    public void setQuantityCharacteristicID(GUID value)
    {
//        get
//        {
//            return m_physicalStructure
//                .getScalarByTag(QuantityCharacteristicIDTag)
//                .getGuid();
//        }
//        set
//        {
            ScalarElement quantityCharacteristicIDElement = m_physicalStructure.getOrAddScalar(QuantityCharacteristicIDTag);
            quantityCharacteristicIDElement.setTypeOfValue(PhysicalType.Guid);
            quantityCharacteristicIDElement.setGuid(value);
            getQuantityCharacteristicID();
//        }
    }

    /// <summary>
    /// Gets the storage method ID, which can be used with
    /// <see cref="StorageMethods"/> to determine how the data is stored.
    /// </summary>
    StorageMethods storageMethodID;
    public StorageMethods getStorageMethodID()
    {
//        get
//        {
            return storageMethodID = StorageMethods.from(m_physicalStructure
                .getScalarByTag(StorageMethodIDTag)
                .getUInt4());
//        }
//        set
//        {
//            ScalarElement storageMethodIDElement = m_physicalStructure.getOrAddScalar(StorageMethodIDTag);
//            storageMethodIDElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            storageMethodIDElement.setUInt4((uint)value);
//        }
    }
    
    public void setStorageMethodID(int value)
    {
//        get
//        {
//            return (StorageMethods)m_physicalStructure
//                .getScalarByTag(StorageMethodIDTag)
//                .getUInt4();
//        }
//        set
//        {
            ScalarElement storageMethodIDElement = m_physicalStructure.getOrAddScalar(StorageMethodIDTag);
            storageMethodIDElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            storageMethodIDElement.setUInt4(value);
            getStorageMethodID();
//        }
    }

    /// <summary>
    /// Gets the value type name of the series.
    /// </summary>
    String valueTypeName;
    public String getValueTypeName()
    {
//        get
//        {
            VectorElement valueTypeNameElement = m_physicalStructure.getVectorByTag(ValueTypeNameTag);

            if (valueTypeNameElement == null)
                return valueTypeName = null;

            return valueTypeName = new String(valueTypeNameElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        }
//        set
//        {
//            byte[] bytes = Encoding.ASCII.GetBytes(value + (char)0);
//            m_physicalStructure.addOrUpdateVector(ValueTypeNameTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setValueTypeName(String value)
    {
//        get
//        {
//            VectorElement valueTypeNameElement = m_physicalStructure.getVectorByTag(ValueTypeNameTag);
//
//            if ((object)valueTypeNameElement == null)
//                return null;
//
//            return Encoding.ASCII.GetString(valueTypeNameElement.GetValues()).Trim((char)0);
//        }
//        set
//        {
            byte[] bytes = (value + '0').getBytes(StandardCharsets.US_ASCII);// Encoding.ASCII.GetBytes(value + (char)0);
            m_physicalStructure.addOrUpdateVector(ValueTypeNameTag, PhysicalType.Char1, bytes);
            getValueTypeName();
//        }
    }

    /// <summary>
    /// Gets the nominal quantity of the series.
    /// </summary>
    double seriesNominalQuantity;
    public double getSeriesNominalQuantity()
    {
//        get
//        {
            ScalarElement seriesNominalQuantityElement = m_physicalStructure.getScalarByTag(SeriesNominalQuantityTag);

            if (seriesNominalQuantityElement == null)
                return seriesNominalQuantity = 0.0D;

            return seriesNominalQuantity = seriesNominalQuantityElement.getReal8();
//        }
//        set
//        {
//            ScalarElement seriesNominalQuantityElement = m_physicalStructure.getOrAddScalar(SeriesNominalQuantityTag);
//            seriesNominalQuantityElement.TypeOfValue = PhysicalType.Real8;
//            seriesNominalQuantityElement.setReal8(value);
//        }
    }
    
    public void setSeriesNominalQuantity(int value)
    {
//        get
//        {
//            ScalarElement seriesNominalQuantityElement = m_physicalStructure.getScalarByTag(SeriesNominalQuantityTag);
//
//            if ((object)seriesNominalQuantityElement == null)
//                return 0.0D;
//
//            return seriesNominalQuantityElement.getReal8();
//        }
//        set
//        {
            ScalarElement seriesNominalQuantityElement = m_physicalStructure.getOrAddScalar(SeriesNominalQuantityTag);
            seriesNominalQuantityElement.setTypeOfValue(PhysicalType.Real8);
            seriesNominalQuantityElement.setReal8(value);
            getSeriesNominalQuantity();
//        }
    }

    /// <summary>
    /// Determines whether an element identified by the
    /// given tag exists in this object's physical structure.
    /// </summary>
    /// <param name="tag">The tag of the element to search for.</param>
    /// <returns>True if the element exists; false otherwise.</returns>
    public boolean hasElement(GUID tag)
    {
        return !m_physicalStructure.getElementsByTag(tag).isEmpty();
    }

    /// <summary>
    /// Indicates whether the current object is equal to another object of the same type.
    /// </summary>
    /// <param name="other">An object to compare with this object.</param>
    /// <returns>true if the current object is equal to the <paramref name="other"/> parameter; otherwise, false.</returns>
    public boolean equals(SeriesDefinition other)
    {
        if (other == null)
            return false;

        return m_physicalStructure == other.m_physicalStructure;
    }

    /// <summary>
    /// Determines whether the specified <see cref="T:System.Object"/> is equal to the current <see cref="T:System.Object"/>.
    /// </summary>
    /// <param name="obj">The object to compare with the current object. </param>
    /// <returns>true if the specified object  is equal to the current object; otherwise, false.</returns>
    /// <filterpriority>2</filterpriority>
    public boolean quals(Object obj)
    {
        return equals((SeriesDefinition) obj);
    }

    /// <summary>
    /// Serves as a hash function for a particular type. 
    /// </summary>
    /// <returns>A hash code for the current <see cref="T:System.Object"/>.</returns>
    /// <filterpriority>2</filterpriority>
    public int hashCode()
    {
        return m_physicalStructure.hashCode();
    }

    // Static Fields

    /// <summary>
    /// Tag that identifies the value type ID of the series.
    /// </summary>
    public static GUID ValueTypeIDTag = new GUID("b48d859c-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the quantity units ID of the series.
    /// </summary>
    public static GUID QuantityUnitsIDTag = new GUID("b48d859b-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the characteristic ID of the series.
    /// </summary>
    public static GUID QuantityCharacteristicIDTag = new GUID("3d786f9e-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the storage method ID of the series.
    /// </summary>
    public static GUID StorageMethodIDTag = new GUID("b48d85a1-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the value type name of the series.
    /// </summary>
    public static GUID ValueTypeNameTag = new GUID("b48d859d-f5f5-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the nominal quantity of the series.
    /// </summary>
    public static GUID SeriesNominalQuantityTag = new GUID("0fa118c8-cb4a-11d2-b30b-fe25cb9a1760");

}
