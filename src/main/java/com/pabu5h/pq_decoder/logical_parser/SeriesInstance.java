package com.pabu5h.pq_decoder.logical_parser;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pabu5h.pq_decoder.logical_parser.SeriesDefinition.StorageMethods;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.Element;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.physical_parser.VectorElement;
import com.pabu5h.pq_decoder.util.GUID;

public class SeriesInstance {

//    #region [ Members ]

    // Fields
    private CollectionElement m_physicalStructure;
    private ChannelInstance m_channel;
    private SeriesDefinition m_definition;

//    #endregion

//    #region [ Constructors ]

    /// <summary>
    /// Creates a new instance of the <see cref="SeriesInstance"/> class.
    /// </summary>
    /// <param name="physicalStructure">The physical structure of the series instance.</param>
    /// <param name="channel">The channel instance that this series instance resides in.</param>
    /// <param name="definition">The series definition that defines this series instance.</param>
    public SeriesInstance(CollectionElement physicalStructure, ChannelInstance channel, SeriesDefinition definition)
    {
        m_physicalStructure = physicalStructure;
        m_channel = channel;
        m_definition = definition;
    }

//    #endregion

//    #region [ Properties ]

    /// <summary>
    /// Gets the physical structure of the series instance.
    /// </summary>
    public CollectionElement getPhysicalStructure()
    {
//        get
//        {
            return m_physicalStructure;
//        }
    }

    /// <summary>
    /// Gets the channel instance in which the series instance resides.
    /// </summary>
    @JsonIgnore
    public ChannelInstance getChannel()
    {
//        get
//        {
            return m_channel;
//        }
    }

    /// <summary>
    /// Gets the series definition that defines the series.
    /// </summary>
    public SeriesDefinition getDefinition()
    {
//        get
//        {
            return m_definition;
//        }
    }

    /// <summary>
    /// Gets the value by which to scale the values in
    /// order to restore the original data values.
    /// </summary>
    public ScalarElement getSeriesScale(GUID SeriesScaleTag)
    {
//        get
//        {
            return m_physicalStructure.getScalarByTag(SeriesScaleTag) == null ? getSeriesShareSeries(0).getSeriesScale(SeriesScaleTag) : m_physicalStructure.getScalarByTag(SeriesScaleTag);
//                ?? SeriesShareSeries?.SeriesScale;
//        }
//        set
//        {
//            value.TagOfElement = SeriesScaleTag;
//            m_physicalStructure.RemoveElementsByTag(SeriesScaleTag);
//            m_physicalStructure.AddElement(value);
//        }
    }
    
    public void setSeriesScale(CollectionElement value)
    {
//        get
//        {
//            return m_physicalStructure.getScalarByTag(SeriesScaleTag)
//                ?? SeriesShareSeries?.SeriesScale;
//        }
//        set
//        {
            value.setTagOfElement(SeriesScaleTag);
            m_physicalStructure.removeElementsByTag(SeriesScaleTag);
            m_physicalStructure.addElement(value);
//        }
    }

    /// <summary>
    /// Gets the value added to the values in order
    /// to restore the original data values.
    /// </summary>
    public ScalarElement getSeriesOffset()
    {
//        get
//        {
            return m_physicalStructure.getScalarByTag(SeriesOffsetTag) == null ? getSeriesShareSeries(0).getSeriesOffset() : m_physicalStructure.getScalarByTag(SeriesOffsetTag);
//                ?? SeriesShareSeries?.SeriesOffset;
//        }
//        set
//        {
//            value.TagOfElement = SeriesOffsetTag;
//            m_physicalStructure.RemoveElementsByTag(SeriesOffsetTag);
//            m_physicalStructure.AddElement(value);
//        }
    }
    
    public void setSeriesOffset(CollectionElement value)
    {
//        get
//        {
//            return m_physicalStructure.getScalarByTag(SeriesOffsetTag)
//                ?? SeriesShareSeries?.SeriesOffset;
//        }
//        set
//        {
            value.setTagOfElement(SeriesOffsetTag);
            m_physicalStructure.removeElementsByTag(SeriesOffsetTag);
            m_physicalStructure.addElement(value);
//        }
    }

    /// <summary>
    /// Gets the values contained in this series instance.
    /// </summary>
    public VectorElement getSeriesValues()
    {
//        get
//        {
            SeriesInstance seriesShareSeries = getSeriesShareSeries(0);

            return (seriesShareSeries != null)
                ? seriesShareSeries.getSeriesValues()
                : m_physicalStructure.getVectorByTag(SeriesValuesTag);
//        }
//        set
//        {
//            value.TagOfElement = SeriesValuesTag;
//            m_physicalStructure.RemoveElementsByTag(SeriesValuesTag);
//            m_physicalStructure.AddElement(value);
//        }
    }
    
    public void setSeriesValues(Element value)
    {
//        get
//        {
//            SeriesInstance seriesShareSeries = SeriesShareSeries;
//
//            return ((object)seriesShareSeries != null)
//                ? seriesShareSeries.SeriesValues
//                : m_physicalStructure.getVectorByTag(SeriesValuesTag);
//        }
//        set
//        {
            value.setTagOfElement(SeriesValuesTag);
            m_physicalStructure.removeElementsByTag(SeriesValuesTag);
            m_physicalStructure.addElement(value);
//        }
    }

    /// <summary>
    /// Gets the original data values, after expanding
    /// sequences and scale and offset modifications.
    /// </summary>
    public List<Object> getOriginalValues()
    {
//        get
//        {
            return null;// GetOriginalValues();
//        }
    }

    /// <summary>
    /// Gets the index of the channel that owns the series to be shared.
    /// </summary>
    public Integer getSeriesShareChannelIndex()
    {
//        get
//        {
            ScalarElement seriesShareChannelIndexScalar = m_physicalStructure
                .getScalarByTag(SeriesShareChannelIndexTag);

            return (seriesShareChannelIndexScalar != null)
                ? seriesShareChannelIndexScalar.getUInt4()
                : null;
//        }
//        set
//        {
//            if (!value.HasValue)
//            {
//                m_physicalStructure.RemoveElementsByTag(SeriesShareChannelIndexTag);
//            }
//            else
//            {
//                ScalarElement seriesShareChannelIndexScalar = m_physicalStructure
//                    .getOrAddScalar(SeriesShareChannelIndexTag);
//
//                seriesShareChannelIndexScalar.TypeOfValue = PhysicalType.UnsignedInteger4;
//                seriesShareChannelIndexScalar.setUInt4(value.getValueOrDefault());
//            }
//        }
    }
    
    public void setSeriesShareChannelIndex(Integer value)
    {
//        get
//        {
//            ScalarElement seriesShareChannelIndexScalar = m_physicalStructure
//                .getScalarByTag(SeriesShareChannelIndexTag);
//
//            return ((object)seriesShareChannelIndexScalar != null)
//                ? seriesShareChannelIndexScalar.getUInt4()
//                : (uint?)null;
//        }
//        set
//        {
            if (/*!value.HasValue*/ value == null)
            {
                m_physicalStructure.removeElementsByTag(SeriesShareChannelIndexTag);
            }
            else
            {
                ScalarElement seriesShareChannelIndexScalar = m_physicalStructure
                    .getOrAddScalar(SeriesShareChannelIndexTag);

                seriesShareChannelIndexScalar.setTypeOfValue(PhysicalType.UnsignedInteger4);
				seriesShareChannelIndexScalar.setUInt4(/* value.getValueOrDefault() */value);
            }
//        }
    }

    /// <summary>
    /// Gets the index of the series to be shared.
    /// </summary>
    public Integer getSeriesShareSeriesIndex()
    {
//        get
//        {
            ScalarElement seriesShareSeriesIndexScalar = m_physicalStructure
                .getScalarByTag(SeriesShareSeriesIndexTag);

            return (seriesShareSeriesIndexScalar != null)
                ? seriesShareSeriesIndexScalar.getUInt4()
                : null;
//        }
//        set
//        {
//            if (!value.HasValue)
//            {
//                m_physicalStructure.RemoveElementsByTag(SeriesShareSeriesIndexTag);
//            }
//            else
//            {
//                ScalarElement seriesShareSeriesIndexScalar = m_physicalStructure
//                    .getOrAddScalar(SeriesShareSeriesIndexTag);
//
//                seriesShareSeriesIndexScalar.TypeOfValue = PhysicalType.UnsignedInteger4;
//                seriesShareSeriesIndexScalar.setUInt4(value.getValueOrDefault());
//            }
//        }
    }
    
    public void setSeriesShareSeriesIndex(Integer value)
    {
//        get
//        {
//            ScalarElement seriesShareSeriesIndexScalar = m_physicalStructure
//                .getScalarByTag(SeriesShareSeriesIndexTag);
//
//            return ((object)seriesShareSeriesIndexScalar != null)
//                ? seriesShareSeriesIndexScalar.getUInt4()
//                : (uint?)null;
//        }
//        set
//        {
            if (/*!value.HasValue*/ value == null)
            {
                m_physicalStructure.removeElementsByTag(SeriesShareSeriesIndexTag);
            }
            else
            {
                ScalarElement seriesShareSeriesIndexScalar = m_physicalStructure
                    .getOrAddScalar(SeriesShareSeriesIndexTag);

                seriesShareSeriesIndexScalar.setTypeOfValue(PhysicalType.UnsignedInteger4);
				seriesShareSeriesIndexScalar.setUInt4(value/* .getValueOrDefault() */);
            }
//        }
    }

    /// <summary>
    /// Gets the channel that owns the series to be shared.
    /// </summary>
    public ChannelInstance getSeriesShareChannel(int SeriesShareChannelIndex)
    {
//        get
//        {
            Integer seriesShareChannelIndex = SeriesShareChannelIndex;

            return (seriesShareChannelIndex != null)
                ? m_channel.getObservationRecord().getChannelInstances().get((int)seriesShareChannelIndex)
                : null;
//        }
    }

    /// <summary>
    /// Gets the series to be shared.
    /// </summary>
    public SeriesInstance getSeriesShareSeries(int SeriesShareSeriesIndex)
    {
//        get
//        {
            Integer seriesShareSeriesIndex = SeriesShareSeriesIndex;
            ChannelInstance seriesShareChannel = getSeriesShareChannel(seriesShareSeriesIndex);
            SeriesInstance seriesShareSeries = null;

            if ((seriesShareSeriesIndex != null) && (seriesShareChannel != null))
                seriesShareSeries = seriesShareChannel.getSeriesInstances().get((int)seriesShareSeriesIndex);

            return seriesShareSeries;
//        }
    }

//    #endregion

//    #region [ Methods ]

    /// <summary>
    /// Sets the raw values to be written to the PQDIF file as the <see cref="SeriesValues"/>.
    /// </summary>
    /// <param name="values">The values to be written to the PQDIF file.</param>
    public void setValues(List<Integer> values)
    {
        VectorElement seriesValuesElement;

        seriesValuesElement = new VectorElement();
//        {
//            Size = values.Count,
//            TagOfElement = SeriesValuesTag,
//            TypeOfValue = PhysicalTypeExtensions.getPhysicalType(values[0].getType())
//        };
        seriesValuesElement.setSize(values.size());
        seriesValuesElement.setTagOfElement(SeriesValuesTag);
		seriesValuesElement.setTypeOfValue(/* PhysicalTypeExtensions */PhysicalType.fromValue(values.get(0).byteValue()));

        for (int i = 0; i < values.size(); i++) {
        	seriesValuesElement.set(i, values.get(i));
        }
            

        setSeriesValues(seriesValuesElement);
    }

    /// <summary>
    /// Sets the values to be written to the PQDIF
    /// file for the increment storage method.
    /// </summary>
    /// <param name="start">The start of the increment.</param>
    /// <param name="count">The number of values in the series.</param>
    /// <param name="increment">The amount by which to increment each value in the series.</param>
    public void setValues(Integer start, Object count, Object increment)
    {
        VectorElement seriesValuesElement;

        seriesValuesElement = new VectorElement();
        seriesValuesElement.setSize(3);
        seriesValuesElement.setTagOfElement(SeriesValuesTag);
        seriesValuesElement.setTypeOfValue(PhysicalType.fromValue(start.byteValue()));
//        {
//            Size = 3,
//            TagOfElement = SeriesValuesTag,
//            TypeOfValue = PhysicalTypeExtensions.getPhysicalType(start.getType())
//        };

        seriesValuesElement.set(0, start);
        seriesValuesElement.set(1, count);
        seriesValuesElement.set(2, increment);

        setSeriesValues(seriesValuesElement);
    }

    /// <summary>
    /// Indicates whether the current object is equal to another object of the same type.
    /// </summary>
    /// <param name="other">An object to compare with this object.</param>
    /// <returns>true if the current object is equal to the <paramref name="other"/> parameter; otherwise, false.</returns>
    public boolean equals(SeriesInstance other)
    {
        if (other == null)
            return false;

        return m_physicalStructure == other.m_physicalStructure;
    }

    /// <summary>
    /// Determines whether the specified <see cref="SeriesInstance"/> is equal to the current <see cref="SeriesInstance"/>.
    /// </summary>
    /// <param name="obj">The object to compare with the current object. </param>
    /// <returns>true if the specified object  is equal to the current object; otherwise, false.</returns>
    /// <filterpriority>2</filterpriority>
    public boolean equals(Object obj)
    {
        return equals((SeriesInstance) obj);
    }

    /// <summary>
    /// Serves as a hash function for a particular type. 
    /// </summary>
    /// <returns>A hash code for the current <see cref="SeriesInstance"/>.</returns>
    /// <filterpriority>2</filterpriority>
    public int hashCode()
    {
        return m_physicalStructure.hashCode();
    }

    /// <summary>
    /// Gets the original data values by expanding
    /// sequences and applying scale and offset.
    /// </summary>
    /// <returns>A list of the original data values.</returns>
    private List<Number> getOriginalValues1()
    {
        List<Number> values = new ArrayList<>();
        VectorElement valuesVector = getSeriesValues();
        StorageMethods storageMethods = getDefinition().getStorageMethodID();

        boolean incremented = (storageMethods.value & StorageMethods.Increment.value) != 0;

        boolean scaled = (storageMethods.value & StorageMethods.Scaled.value) != 0;
        Number offset = (getSeriesOffset() != null) ? (int) getSeriesOffset().get() : 0;
        Number scale = (getSeriesScale(SeriesScaleTag) != null) ? (int) getSeriesScale(SeriesOffsetTag).get() : 1;
        Number value;

        if (!scaled)
        {
            offset = 0;
            scale = 1;
        }

        if (incremented)
        {
            Number rateCount = (Number) valuesVector.get(0);

            if (rateCount.intValue() > 0)
            {
            	Number zero = rateCount.intValue() - rateCount.intValue();
            	Number one = rateCount.intValue() / rateCount.intValue();
            	Number start = zero;

                for (int i = 0; i < rateCount.intValue(); i++)
                {
                    int countIndex = (i * 2) + 1;
                    int incrementIndex = (i * 2) + 2;
                    Number count = (Number) valuesVector.get(countIndex);
                    Number increment = (Number) valuesVector.get(incrementIndex);

                    for (int j = zero.intValue(); j < count.intValue(); j = j + one.intValue())
                        values.add((start.intValue() + j * increment.intValue()));

                    start = count.intValue() * increment.intValue();
                }
            }
        }
        else
        {
            for (int i = 0; i < valuesVector.getByteSize(); i++)
                values.add((Number) valuesVector.get(i));
        }

        if (valuesVector.getTypeOfValue() != PhysicalType.Timestamp)
        {
            for (int i = 0; i < values.size(); i++)
            {
                value = values.get(i);
                values.set(i, offset.intValue() + (value.intValue() * scale.intValue()));
            }

            ApplyTransducerRatio(values);
        }

        return values;
    }

    private void ApplyTransducerRatio(List<Number> values)
    {
        ChannelSetting channelSetting;
        double ratio;
        Number value;

        if (getChannel().getObservationRecord().getSettings() == null)
            return;

        if (!getChannel().getObservationRecord().getSettings().getUseTransducer())
            return;

        channelSetting = getChannel().getSetting();

        if (channelSetting == null)
            return;

        if (!channelSetting.hasElement(ChannelSetting.XDSystemSideRatioTag))
            return;

        if (!channelSetting.hasElement(ChannelSetting.XDMonitorSideRatioTag))
            return;

        ratio = channelSetting.getXDSystemSideRatio() / channelSetting.getXDMonitorSideRatio();

        for (int i = 0; i < values.size(); i++)
        {
            value = values.get(i);
            value = value.intValue() * ratio;
            values.set(i, value);
        }
    }

//    #endregion

//    #region [ Static ]

    // Static Fields

    /// <summary>
    /// Tag that identifies the scale value to apply to the series.
    /// </summary>
    public static GUID SeriesScaleTag = new GUID("3d786f96-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the offset value to apply to the series.
    /// </summary>
    public static GUID SeriesOffsetTag = new GUID("3d786f97-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the values contained in the series.
    /// </summary>
    public static GUID SeriesValuesTag = new GUID("3d786f99-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the index of the channel that owns the series to be shared.
    /// </summary>
    public static GUID SeriesShareChannelIndexTag = new GUID("8973861f-f1c3-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the index of the series to be shared.
    /// </summary>
    public static GUID SeriesShareSeriesIndexTag = new GUID("89738620-f1c3-11cf-9d89-0080c72e70a3");

//    #endregion
}
