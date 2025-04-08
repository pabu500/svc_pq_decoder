package com.pabu5h.pq_decoder.logical_parser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.util.GUID;

public class ChannelSetting {

//	#region [ Members ]

    // Fields
    private CollectionElement m_physicalStructure;
    private MonitorSettingsRecord m_monitorSettingsRecord;

//    #endregion

//    #region [ Constructors ]

    /// <summary>
    /// Creates a new instance of the <see cref="ChannelSetting"/> class.
    /// </summary>
    /// <param name="physicalStructure">The collection element which is the physical structure of the channel setting.</param>
    /// <param name="monitorSettingsRecord">The monitor settings record in which the channel setting resides.</param>
    public ChannelSetting(CollectionElement physicalStructure, MonitorSettingsRecord monitorSettingsRecord)
    {
        m_physicalStructure = physicalStructure;
        m_monitorSettingsRecord = monitorSettingsRecord;
        
        this.getChannelDefinitionIndex();
        this.getMonitorSettingsRecord();
        this.getPhysicalStructure();
        this.getXDMonitorSideRatio();
        this.getXDSystemSideRatio();
    }

//    #endregion

//    #region [ Properties ]

    /// <summary>
    /// Gets the physical structure of the channel setting.
    /// </summary>
    public CollectionElement getPhysicalStructure()
    {
//        get
//        {
            return m_physicalStructure;
//        }
    }

    /// <summary>
    /// Gets the monitor settings record in which the channel setting resides.
    /// </summary>
    MonitorSettingsRecord monitorSettingsRecord;
    @JsonIgnore
    public MonitorSettingsRecord getMonitorSettingsRecord()
    {
//        get
//        {
            return monitorSettingsRecord = m_monitorSettingsRecord;
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
    /// Gets the system-side part of the transducer ratio.
    /// </summary>
    double xdSystemSideRatio;
    public double getXDSystemSideRatio()
    {
//        get
//        {
            ScalarElement xdSystemSideRatioElement = m_physicalStructure.getScalarByTag(XDSystemSideRatioTag);

            if (xdSystemSideRatioElement == null)
                return xdSystemSideRatio = 1.0D;

            return xdSystemSideRatio = xdSystemSideRatioElement.getReal8();
//        }
//        set
//        {
//            ScalarElement xdSystemSideRatioElement = m_physicalStructure.getOrAddScalar(XDSystemSideRatioTag);
//            xdSystemSideRatioElement.TypeOfValue = PhysicalType.Real8;
//            xdSystemSideRatioElement.setReal8(value);
//        }
    }
    
    public void setXDSystemSideRatio(int value)
    {
//        get
//        {
//            ScalarElement xdSystemSideRatioElement = m_physicalStructure.getScalarByTag(XDSystemSideRatioTag);
//
//            if ((object)xdSystemSideRatioElement == null)
//                return 1.0D;
//
//            return xdSystemSideRatioElement.getReal8();
//        }
//        set
//        {
            ScalarElement xdSystemSideRatioElement = m_physicalStructure.getOrAddScalar(XDSystemSideRatioTag);
            xdSystemSideRatioElement.setTypeOfValue(PhysicalType.Real8);
            xdSystemSideRatioElement.setReal8(value);
            getXDSystemSideRatio();
//        }
    }

    /// <summary>
    /// Gets the monitor-side part of the transducer ratio.
    /// </summary>
    double xdMonitorSideRatio;
    public double getXDMonitorSideRatio()
    {
//        get
//        {
            ScalarElement xdMonitorSideRatioElement = m_physicalStructure.getScalarByTag(XDMonitorSideRatioTag);

            if (xdMonitorSideRatioElement == null)
                return xdMonitorSideRatio = 1.0D;

            return xdMonitorSideRatio = xdMonitorSideRatioElement.getReal8();
//        }
//        set
//        {
//            ScalarElement xdMonitorSideRatioElement = m_physicalStructure.getOrAddScalar(XDMonitorSideRatioTag);
//            xdMonitorSideRatioElement.TypeOfValue = PhysicalType.Real8;
//            xdMonitorSideRatioElement.setReal8(value);
//        }
    }
    
    public void setXDMonitorSideRatio(int value)
    {
//        get
//        {
//            ScalarElement xdMonitorSideRatioElement = m_physicalStructure.getScalarByTag(XDMonitorSideRatioTag);
//
//            if ((object)xdMonitorSideRatioElement == null)
//                return 1.0D;
//
//            return xdMonitorSideRatioElement.getReal8();
//        }
//        set
//        {
            ScalarElement xdMonitorSideRatioElement = m_physicalStructure.getOrAddScalar(XDMonitorSideRatioTag);
            xdMonitorSideRatioElement.setTypeOfValue(PhysicalType.Real8);
            xdMonitorSideRatioElement.setReal8(value);
            getXDMonitorSideRatio();
//        }
    }

//    #endregion

//    #region [ Methods ]

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

//    #endregion

//    #region [ Static ]

    // Static Fields

    /// <summary>
    /// Tag that identifies the system side ratio.
    /// </summary>
    public static GUID XDSystemSideRatioTag = new GUID("62f2818a-f9c4-11cf-9d89-0080c72e70a3");

    /// <summary>
    /// Tag that identifies the monitor side ratio.
    /// </summary>
    public static GUID XDMonitorSideRatioTag = new GUID("62f2818b-f9c4-11cf-9d89-0080c72e70a3");

//    #endregion
}
