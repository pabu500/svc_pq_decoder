package com.pabu5h.pq_decoder.logical_parser;

import java.util.List;
import java.util.stream.Collectors;

import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.util.DateTime;
import com.pabu5h.pq_decoder.util.GUID;

public class MonitorSettingsRecord {

//	#region [ Members ]

	        // Fields
    private Record m_physicalRecord;

//    #endregion

//    #region [ Constructors ]

    /// <summary>
    /// Creates a new instance of the <see cref="MonitorSettingsRecord"/> class.
    /// </summary>
    /// <param name="physicalRecord">The physical structure of the monitor settings record.</param>
    private MonitorSettingsRecord(Record physicalRecord)
    {
        m_physicalRecord = physicalRecord;
        this.getNominalFrequency();
        this.getChannelSettings();
        this.getEffective();
        this.getPhysicalRecord();
        this.getTimeInstalled();
        this.getUseCalibration();
        this.getUseTransducer();
    }

//    #endregion

//    #region [ Properties ]

    /// <summary>
    /// Gets the physical record of the monitor settings record.
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
    /// Gets or sets the date time at which these settings become effective.
    /// </summary>
    DateTime effective;
    public DateTime getEffective()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            return effective = collectionElement.getScalarByTag(EffectiveTag).getTimestamp();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement effectiveElement = collectionElement.getOrAddScalar(EffectiveTag);
//            effectiveElement.TypeOfValue = PhysicalType.Timestamp;
//            effectiveElement.setTimestamp(value);
//        }
    }
    
    public void setEffective(DateTime value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            return collectionElement.getScalarByTag(EffectiveTag).getTimestamp();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement effectiveElement = collectionElement.getOrAddScalar(EffectiveTag);
            effectiveElement.setTypeOfValue(PhysicalType.Timestamp);
            effectiveElement.setTimestamp(value);
            getEffective();
//        }
    }

    /// <summary>
    /// Gets or sets the time at which the settings were installed.
    /// </summary>
    DateTime timeInstalled;
    public DateTime getTimeInstalled()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            return timeInstalled = collectionElement.getScalarByTag(TimeInstalledTag).getTimestamp();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement timeInstalledElement = collectionElement.getOrAddScalar(TimeInstalledTag);
//            timeInstalledElement.TypeOfValue = PhysicalType.Timestamp;
//            timeInstalledElement.setTimestamp(value);
//        }
    }
    
    public void setTimeInstalled(DateTime value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            return collectionElement.getScalarByTag(TimeInstalledTag).getTimestamp();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement timeInstalledElement = collectionElement.getOrAddScalar(TimeInstalledTag);
            timeInstalledElement.setTypeOfValue(PhysicalType.Timestamp);
            timeInstalledElement.setTimestamp(value);
            getTimeInstalled();
    }

    /// <summary>
    /// Gets or sets the flag that determines whether the
    /// calibration settings need to be applied before using
    /// the values recorded by this monitor.
    /// </summary>
    boolean useCalibration;
    public boolean getUseCalibration()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            return useCalibration = collectionElement.getScalarByTag(UseCalibrationTag).getBool4();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement useCalibrationElement = collectionElement.getOrAddScalar(UseCalibrationTag);
//            useCalibrationElement.TypeOfValue = PhysicalType.Boolean4;
//            useCalibrationElement.setBool4(value);
//        }
    }
    
    public void setUseCalibration(boolean value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            return collectionElement.getScalarByTag(UseCalibrationTag).getBool4();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement useCalibrationElement = collectionElement.getOrAddScalar(UseCalibrationTag);
            useCalibrationElement.setTypeOfValue(PhysicalType.Boolean4);
            useCalibrationElement.setBool4(value);
            getUseCalibration();
//        }
    }

    /// <summary>
    /// Gets or sets the flag that determines whether the
    /// transducer ratio needs to be applied before using
    /// the values recorded by this monitor.
    /// </summary>
    boolean useTransducer;
    public boolean getUseTransducer()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            return useTransducer = collectionElement.getScalarByTag(UseTransducerTag).getBool4();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement useTransducerElement = collectionElement.getOrAddScalar(UseTransducerTag);
//            useTransducerElement.TypeOfValue = PhysicalType.Boolean4;
//            useTransducerElement.setBool4(value);
//        }
    }
    
    public void setUseTransducer(boolean value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            return collectionElement.getScalarByTag(UseTransducerTag).getBool4();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement useTransducerElement = collectionElement.getOrAddScalar(UseTransducerTag);
            useTransducerElement.setTypeOfValue(PhysicalType.Boolean4);
            useTransducerElement.setBool4(value);
            getUseTransducer();
//        }
    }

    /// <summary>
    /// Gets or sets the settings for the channels defined in the data source.
    /// </summary>
    List<ChannelSetting> channelSettings;
    public List<ChannelSetting> getChannelSettings()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            CollectionElement channelSettingsArray = collectionElement.getCollectionByTag(ChannelSettingsArrayTag);

            if (channelSettingsArray == null)
                return channelSettings = null;

            return channelSettings = channelSettingsArray
                .getElementsByTag(OneChannelSettingTag)
                .stream()
                .map(collection -> new ChannelSetting((CollectionElement) collection, this))
//                .Cast<CollectionElement>()
//                .Select(collection => new ChannelSetting(collection, this))
                .collect(Collectors.toList());
//        }
    }

    /// <summary>
    /// Gets or sets nominal frequency.
    /// </summary>
    double getNominalFrequency;
    public double getNominalFrequency()
    {
//        get
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement nominalFrequencyElement = collectionElement.getScalarByTag(NominalFrequencyTag);

            if (nominalFrequencyElement == null)
                return getNominalFrequency = DefaultNominalFrequency;

            return getNominalFrequency = nominalFrequencyElement.getReal8();
//        }
//        set
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement nominalFrequencyElement = collectionElement.getOrAddScalar(NominalFrequencyTag);
//            nominalFrequencyElement.TypeOfValue = PhysicalType.Real8;
//            nominalFrequencyElement.setReal8(value);
//        }
    }
    
    public void setNominalFrequency(Double value)
    {
//        get
//        {
//            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
//            ScalarElement nominalFrequencyElement = collectionElement.getScalarByTag(NominalFrequencyTag);
//
//            if ((object)nominalFrequencyElement == null)
//                return DefaultNominalFrequency;
//
//            return nominalFrequencyElement.getReal8();
//        }
//        set
//        {
            CollectionElement collectionElement = m_physicalRecord.getBody().getCollection();
            ScalarElement nominalFrequencyElement = collectionElement.getOrAddScalar(NominalFrequencyTag);
            nominalFrequencyElement.setTypeOfValue(PhysicalType.Real8);
            nominalFrequencyElement.setReal8(value);
            getNominalFrequency();
//        }
    }

//    #endregion

//    #region [ Methods ]

    /// <summary>
    /// Adds a new channel setting to the collection
    /// of channel settings in this monitor settings record.
    /// </summary>
    public ChannelSetting addNewChannelSetting(ChannelDefinition channelDefinition)
    {
        CollectionElement channelSettingElement = new CollectionElement(OneChannelSettingTag);
        ChannelSetting channelSetting = new ChannelSetting(channelSettingElement, this);
        channelSetting.setChannelDefinitionIndex((int)channelDefinition.getDataSource().getChannelDefinitions().indexOf(channelDefinition));

        CollectionElement channelSettingsElement = m_physicalRecord.getBody().getCollection().getCollectionByTag(ChannelSettingsArrayTag);

        if (channelSettingsElement == null)
        {
            channelSettingsElement = new CollectionElement(OneChannelSettingTag);
            m_physicalRecord.getBody().getCollection().addElement(channelSettingsElement);
        }

        channelSettingsElement.addElement(channelSettingElement);

        return channelSetting;
    }

    /// <summary>
    /// Removes the given channel setting from the collection of channel settings.
    /// </summary>
    /// <param name="channelSetting">The channel setting to be removed.</param>
    public void remove(ChannelSetting channelSetting)
    {
        CollectionElement channelSettingsElement = m_physicalRecord.getBody().getCollection().getCollectionByTag(ChannelSettingsArrayTag);
        List<CollectionElement> channelSettingElements;
        ChannelSetting setting;

        if (channelSettingsElement == null)
            return;

        channelSettingElements = channelSettingsElement.getElementsByTag(OneChannelSettingTag)
        		.stream()
        		.map(a -> (CollectionElement) a)
//        		.Cast<CollectionElement>()
        		.collect(Collectors.toList());

        for (CollectionElement channelSettingElement : channelSettingElements)
        {
            setting = new ChannelSetting(channelSettingElement, this);

            if (channelSetting.equals(setting))
                channelSettingsElement.removeElement(channelSettingElement);
        }
    }

//    #endregion

//    #region [ Static ]

    // Static Fields

    /// <summary>
    /// Tag that identifies the time that these settings become effective.
    /// </summary>
    public static GUID EffectiveTag = new GUID("62f28183-f9c4-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the install time.
    /// </summary>
    public static GUID TimeInstalledTag = new GUID("3d786f85-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the flag which determines whether to apply calibration to the series.
    /// </summary>
    public static GUID UseCalibrationTag = new GUID("62f28180-f9c4-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the flag which determines whether to apply transducer adjustments to the series.
    /// </summary>
    public static GUID UseTransducerTag = new GUID("62f28181-f9c4-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the collection of channel settings.
    /// </summary>
    public static GUID ChannelSettingsArrayTag = new GUID("62f28182-f9c4-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies one channel setting in the collection.
    /// </summary>
    public static GUID OneChannelSettingTag = new GUID("3d786f9a-f76e-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the nominal frequency.
    /// </summary>
    public static GUID NominalFrequencyTag = new GUID("0fa118c3-cb4a-11d2-b30b-fe25cb9a1760");

    // Static Properties

    /// <summary>
    /// Gets or sets the default value for the <see cref="NominalFrequency"/>
    /// property when the value is not defined in the PQDIF file.
    /// </summary>
    public static double DefaultNominalFrequency = 60.0D;
    
    public static double getDefaultNominalFrequency() {
    	return DefaultNominalFrequency;
    }
    
    public static void setDefaultNominalFrequency(double in) {
    	DefaultNominalFrequency = in;
    }

    // Static Methods

    /// <summary>
    /// Creates a new monitor settings record from scratch.
    /// </summary>
    /// <returns>The new monitor settings record.</returns>
    public static MonitorSettingsRecord createMonitorSettingsRecord()
    {
        GUID recordTypeTag = Record.getTypeAsTag(RecordType.MonitorSettings);
        Record physicalRecord = new Record(recordTypeTag);
        MonitorSettingsRecord monitorSettingsRecord = new MonitorSettingsRecord(physicalRecord);

        DateTime now = DateTime.utcNow();
        monitorSettingsRecord.setEffective(now);
        monitorSettingsRecord.setTimeInstalled(now);
        monitorSettingsRecord.setUseCalibration(false);
        monitorSettingsRecord.setUseTransducer(false);

        CollectionElement bodyElement = physicalRecord.getBody().getCollection();
        bodyElement.addElement(new CollectionElement(ChannelSettingsArrayTag));

        return monitorSettingsRecord;
    }

    /// <summary>
    /// Creates a new monitor settings record from the given physical record
    /// if the physical record is of type monitor settings. Returns null if
    /// it is not.
    /// </summary>
    /// <param name="physicalRecord">The physical record used to create the monitor settings record.</param>
    /// <returns>The new monitor settings record, or null if the physical record does not define a monitor settings record.</returns>
    public static MonitorSettingsRecord createMonitorSettingsRecord(Record physicalRecord)
    {
        boolean isValidMonitorSettingsRecord = physicalRecord.getHeader().getTypeOfRecord() == RecordType.MonitorSettings;
        return isValidMonitorSettingsRecord ?  new MonitorSettingsRecord(physicalRecord) : null;
    }

//    #endregion

}
