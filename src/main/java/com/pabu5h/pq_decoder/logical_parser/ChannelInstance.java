package com.pabu5h.pq_decoder.logical_parser;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.physical_parser.VectorElement;
import com.pabu5h.pq_decoder.util.GUID;

public class ChannelInstance {

	private CollectionElement m_physicalStructure;
    private ObservationRecord m_observationRecord;

//    #endregion

//    #region [ Constructors ]

    /// <summary>
    /// Creates a new instance of the <see cref="ChannelInstance"/> class.
    /// </summary>
    /// <param name="physicalStructure">The collection element which is the physical structure of the channel instance.</param>
    /// <param name="observationRecord">The observation record in which the channel instance resides.</param>
    public ChannelInstance(CollectionElement physicalStructure, ObservationRecord observationRecord)
    {
        m_physicalStructure = physicalStructure;
        m_observationRecord = observationRecord;
        getChannelDefinitionIndex();
        getChannelGroupID();
        getCrossTriggerDeviceName();
        getDefinition();
        getObservationRecord();
        getPhysicalStructure();
        getSeriesInstances();
        getSetting();
        getTriggerModuleName();
    }

//    #endregion

//    #region [ Properties ]

    /// <summary>
    /// Gets the physical structure of the channel instance.
    /// </summary>
    CollectionElement physicalStructure;
    public CollectionElement getPhysicalStructure()
    {
//        get
//        {
            return physicalStructure = m_physicalStructure;
//        }
    }

    /// <summary>
    /// Gets the observation record in which the channel instance resides.
    /// </summary>
    ObservationRecord observationRecord;
    @JsonIgnore
    public ObservationRecord getObservationRecord()
    {
//        get
//        {
            return observationRecord = m_observationRecord;
//        }
    }

    /// <summary>
    /// Gets the channel definition which defines the channel instance.
    /// </summary>
    @JsonBackReference
    ChannelDefinition definition;
    public ChannelDefinition getDefinition()
    {
//        get
//        {
            return definition = m_observationRecord.getDataSource().getChannelDefinitions().get(channelDefinitionIndex);
//        }
    }

    /// <summary>
    /// Gets the channel setting which defines the instrument settings for the channel.
    /// </summary>
    ChannelSetting setting;
    public ChannelSetting getSetting()
    {
//        get
//        {
            MonitorSettingsRecord monitorSettings;
            List<ChannelSetting> channelSettings;

            monitorSettings = m_observationRecord.getSettings();

            if (monitorSettings == null)
                return setting = null;

            channelSettings = monitorSettings.getChannelSettings();

            if (channelSettings == null)
                return setting = null;
            
            return setting = channelSettings.stream()
            .filter(channelSetting -> channelSetting.getChannelDefinitionIndex() == channelDefinitionIndex)
            .findFirst()
            .orElse(channelSettings.get(0));

//            return channelSettings.FirstOrDefault(channelSetting -> channelSetting.ChannelDefinitionIndex == ChannelDefinitionIndex);
//        }
    }

    /// <summary>
    /// Gets the index of the <see cref="ChannelDefinition"/>
    /// which defines the channel instance.
    /// </summary>
    int channelDefinitionIndex;
    public int getChannelDefinitionIndex()
    {
//        get
//        {
            return channelDefinitionIndex = m_physicalStructure
                .getScalarByTag(ChannelDefinition.ChannelDefinitionIndexTag)
                .getUInt4();
//        }
//        set
//        {
//            ScalarElement channelDefinitionIndexElement = m_physicalStructure.getOrAddScalar(ChannelDefinition.ChannelDefinitionIndexTag);
//            channelDefinitionIndexElement.TypeOfValue = PhysicalType.UnsignedInteger4;
//            channelDefinitionIndexElement.setUInt4(value);
//        }
    }
    
    public void setChannelDefinitionIndex(int value)
    {
//        get
//        {
//            return m_physicalStructure
//                .getScalarByTag(ChannelDefinition.ChannelDefinitionIndexTag)
//                .getUInt4();
//        }
//        set
//        {
            ScalarElement channelDefinitionIndexElement = m_physicalStructure.getOrAddScalar(ChannelDefinition.ChannelDefinitionIndexTag);
            channelDefinitionIndexElement.setTypeOfValue(PhysicalType.UnsignedInteger4);
            channelDefinitionIndexElement.setUInt4(value);
            getChannelDefinitionIndex();
//        }
    }

    /// <summary>
    /// Gets the identifier for the harmonic or
    /// interharmonic group represented by this channel.
    /// </summary>
    short channelGroupID;
    public short getChannelGroupID()
    {
//        get
//        {
            ScalarElement channelGroupIDElement = m_physicalStructure.getScalarByTag(ChannelGroupIDTag);

            if (channelGroupIDElement == null)
                return channelGroupID = 0;

            return channelGroupID = channelGroupIDElement.getInt2();
//        }
//        set
//        {
//            ScalarElement channelGroupIDElement = m_physicalStructure.getOrAddScalar(ChannelGroupIDTag);
//            channelGroupIDElement.TypeOfValue = PhysicalType.Integer2;
//            channelGroupIDElement.setInt2(value);
//        }
    }
    
    public void setChannelGroupID(int value)
    {
//        get
//        {
//            ScalarElement channelGroupIDElement = m_physicalStructure.getScalarByTag(ChannelGroupIDTag);
//
//            if ((object)channelGroupIDElement == null)
//                return 0;
//
//            return channelGroupIDElement.getInt2();
//        }
//        set
//        {
            ScalarElement channelGroupIDElement = m_physicalStructure.getOrAddScalar(ChannelGroupIDTag);
            channelGroupIDElement.setTypeOfValue(PhysicalType.Integer2);
            channelGroupIDElement.setInt2(value);
            getChannelGroupID();
//        }
    }

    /// <summary>
    /// Gets the name of the of a device specific code or hardware
    /// module, algorithm, or rule not necessarily channel based
    /// that cause this channel to be recorded.
    /// </summary>
    String triggerModuleName;
    public String getTriggerModuleName()
    {
//        get
//        {
            VectorElement moduleNameElement = m_physicalStructure.getVectorByTag(ChannelTriggerModuleNameTag);

            if (moduleNameElement == null)
                return triggerModuleName = null;

            return triggerModuleName = new String(moduleNameElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        }
//        set
//        {
//            byte[] bytes = Encoding.ASCII.getBytes(value + (char)0);
//            m_physicalStructure.AddOrUpdateVector(ChannelTriggerModuleNameTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setTriggerModuleName(String value)
    {
//        get
//        {
//            VectorElement moduleNameElement = m_physicalStructure.getVectorByTag(ChannelTriggerModuleNameTag);
//
//            if ((object)moduleNameElement == null)
//                return null;
//
//            return Encoding.ASCII.getString(moduleNameElement.getValues()).Trim((char)0);
//        }
//        set
//        {
            byte[] bytes = (value + (char)0).getBytes(StandardCharsets.US_ASCII);
            m_physicalStructure.addOrUpdateVector(ChannelTriggerModuleNameTag, PhysicalType.Char1, bytes);
            getTriggerModuleName();
//        }
    }

    /// <summary>
    /// Gets the name of the device involved in
    /// an external cross trigger scenario.
    /// </summary>
    String crossTriggerDeviceName;
    public String getCrossTriggerDeviceName()
    {
//        get
//        {
            VectorElement deviceNameElement = m_physicalStructure.getVectorByTag(CrossTriggerDeviceNameTag);

            if (deviceNameElement == null)
                return crossTriggerDeviceName = null;

            return crossTriggerDeviceName = new String(deviceNameElement.getValues(), StandardCharsets.US_ASCII).replaceAll("^0+", "").replaceAll("0+$", "");
//        }
//        set
//        {
//            byte[] bytes = Encoding.ASCII.getBytes(value + (char)0);
//            m_physicalStructure.AddOrUpdateVector(CrossTriggerDeviceNameTag, PhysicalType.Char1, bytes);
//        }
    }
    
    public void setCrossTriggerDeviceName(String value)
    {
//        get
//        {
//            VectorElement deviceNameElement = m_physicalStructure.getVectorByTag(CrossTriggerDeviceNameTag);
//
//            if ((object)deviceNameElement == null)
//                return null;
//
//            return Encoding.ASCII.getString(deviceNameElement.getValues()).Trim((char)0);
//        }
//        set
//        {
            byte[] bytes = (value + (char)0).getBytes(StandardCharsets.US_ASCII);
            m_physicalStructure.addOrUpdateVector(CrossTriggerDeviceNameTag, PhysicalType.Char1, bytes);
            getCrossTriggerDeviceName();
//        }
    }

    /// <summary>
    /// Gets the series instances contained in this channel.
    /// </summary>
    List<SeriesInstance> seriesInstances;
    @JsonIgnore
    public List<SeriesInstance> getSeriesInstances()
    {
//        get
//        {
            return seriesInstances = m_physicalStructure
                .getCollectionByTag(SeriesInstancesTag)
                .getElementsByTag(OneSeriesInstanceTag)
                .stream()
                .map(a -> (CollectionElement) a)
                .map(collection -> new SeriesInstance(collection, this, getDefinition().getSeriesDefinitions().get(0)))
//                .Cast<CollectionElement>()
//                .Zip(Definition.SeriesDefinitions, (collection, seriesDefinition) => new SeriesInstance(collection, this, seriesDefinition))
                .collect(Collectors.toList());
//        }
    }

//    #endregion

//    #region [ Methods ]

    /// <summary>
    /// Adds a new series instance to the collection
    /// of series instances in this channel instance.
    /// </summary>
    public SeriesInstance addNewSeriesInstance()
    {
        if (getDefinition().getSeriesDefinitions().size() <= getSeriesInstances().size())
            throw new RuntimeException("Cannot create a series instance without a corresponding series definition.");

        CollectionElement seriesInstanceElement = new CollectionElement(OneSeriesInstanceTag);// { TagOfElement = OneSeriesInstanceTag };
        SeriesDefinition seriesDefinition = getDefinition().getSeriesDefinitions().get(getSeriesInstances().size());
        SeriesInstance seriesInstance = new SeriesInstance(seriesInstanceElement, this, seriesDefinition);
        seriesInstanceElement.addOrUpdateVector(SeriesInstance.SeriesValuesTag, PhysicalType.UnsignedInteger1, new byte[0]);

        CollectionElement seriesInstancesElement = m_physicalStructure.getCollectionByTag(SeriesInstancesTag);

        if (seriesInstancesElement == null)
        {
            seriesInstancesElement = new CollectionElement(SeriesInstancesTag);// { TagOfElement = SeriesInstancesTag };
            m_physicalStructure.addElement(seriesInstancesElement);
        }

        seriesInstancesElement.addElement(seriesInstanceElement);

        return seriesInstance;
    }

    /// <summary>
    /// Removes the given series instance from the collection of series instances.
    /// </summary>
    /// <param name="seriesInstance">The series instance to be removed.</param>
    public void Remove(SeriesInstance seriesInstance)
    {
        CollectionElement seriesInstancesElement;
        List<CollectionElement> seriesInstanceElements;
        List<SeriesDefinition> seriesDefinitions;
        SeriesInstance instance;

        seriesInstancesElement = m_physicalStructure.getCollectionByTag(SeriesInstancesTag);

        if (seriesInstancesElement == null)
            return;

        seriesDefinitions = getDefinition().getSeriesDefinitions();
        seriesInstanceElements = seriesInstancesElement.getElementsByTag(OneSeriesInstanceTag)
        		.stream()
        		.map(a -> (CollectionElement) a)
        		//.Cast<CollectionElement>()
        		.collect(Collectors.toList());

        for (int i = seriesInstanceElements.size(); i >= 0; i--)
        {
            instance = new SeriesInstance(seriesInstanceElements.get(i), this, seriesDefinitions.get(i));

            if (seriesInstance.equals(instance))
                seriesInstancesElement.removeElement(seriesInstancesElement);
        }
    }

    /// <summary>
    /// Indicates whether the current object is equal to another object of the same type.
    /// </summary>
    /// <param name="other">An object to compare with this object.</param>
    /// <returns>true if the current object is equal to the <paramref name="other"/> parameter; otherwise, false.</returns>
    public boolean equals(ChannelInstance other)
    {
        if (other == null)
            return false;

        return m_physicalStructure == other.m_physicalStructure;
    }

    /// <summary>
    /// Determines whether the specified <see cref="ChannelInstance"/> is equal to the current <see cref="ChannelInstance"/>.
    /// </summary>
    /// <param name="obj">The object to compare with the current object. </param>
    /// <returns>true if the specified object  is equal to the current object; otherwise, false.</returns>
    /// <filterpriority>2</filterpriority>
    public boolean equals(Object obj)
    {
        return equals((ChannelInstance) obj);
    }

    /// <summary>
    /// Serves as a hash function for a particular type. 
    /// </summary>
    /// <returns>A hash code for the current <see cref="ChannelInstance"/>.</returns>
    /// <filterpriority>2</filterpriority>
    public int hashCode()
    {
        return m_physicalStructure.hashCode();
    }

//    #endregion

//    #region [ Static ]

    // Static Fields

    /// <summary>
    /// Tag that identifies the channel group ID.
    /// </summary>
    public static GUID ChannelGroupIDTag = new GUID("f90de218-e67b-4cf1-a295-b021a2d46767");

    /// <summary>
    /// Tag that identifies the series instances collection.
    /// </summary>
    public static GUID SeriesInstancesTag = new GUID("3d786f93-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies a single series instance in the collection.
    /// </summary>
    public static GUID OneSeriesInstanceTag = new GUID("3d786f94-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the channel trigger module name.
    /// </summary>
    public static GUID ChannelTriggerModuleNameTag = new GUID("0fa118c6-cb4a-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the cross trigger device name.
    /// </summary>
    public static GUID CrossTriggerDeviceNameTag = new GUID("0fa118c5-cb4a-11cf-9d89-0080c72e70a3");
}
