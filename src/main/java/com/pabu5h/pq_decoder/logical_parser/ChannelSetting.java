package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.util.GUID;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ChannelSetting {
    /**
     * Gets the physical structure of the channel setting.
     */
    private final CollectionElement physicalStructure;

    /**
     * Gets the monitor settings record in which the channel setting resides.
     */
    private final MonitorSettingRecord monitorSettingsRecord;

    /**
     * Tag that identifies the system side ratio.
     */
    public static final UUID xdSystemSideRatioTag = UUID.fromString("62f2818a-f9c4-11cf-9d89-0080c72e70a3");

    /**
     * Tag that identifies the monitor side ratio.
     */
    public static final UUID xdMonitorSideRatioTag = UUID.fromString("62f2818b-f9c4-11cf-9d89-0080c72e70a3");

    /**
     * Creates a new instance of the ChannelSetting class.
     *
     * @param physicalStructure      The collection element which is the physical structure of the channel setting.
     * @param monitorSettingsRecord  The monitor settings record in which the channel setting resides.
     */
    public ChannelSetting(CollectionElement physicalStructure, MonitorSettingRecord monitorSettingsRecord) {
        this.physicalStructure = physicalStructure;
        this.monitorSettingsRecord = monitorSettingsRecord;
    }

    public CollectionElement getPhysicalStructure() {
        return physicalStructure;
    }

    public MonitorSettingRecord getMonitorSettingsRecord() {
        return monitorSettingsRecord;
    }

    /**
     * Gets the index of the ChannelDefinition which defines the channel instance.
     *
     * @return The channel definition index.
     * @throws IllegalStateException if the ChannelDefinitionIndex element is not found.
     */
    public int getChannelDefinitionIndex() {
        return Optional.ofNullable(physicalStructure.getScalarByTag(ChannelDefinition.channelDefinitionIndexTag))
                .orElseThrow(() -> new IllegalStateException("ChannelDefinitionIndex element not found in channel setting."))
                .getUInt4();
    }

    public void setChannelDefinitionIndex(int value) {
        ScalarElement scalar = physicalStructure.getOrAddScalar(ChannelDefinition.channelDefinitionIndexTag);
//        scalar.setTypeOfValue(PhysicalType.UNSIGNED_INTEGER4);
//        scalar.setUInt4(value);
    }

    /**
     * Gets the system-side part of the transducer ratio.
     */
//    public double getXdSystemSideRatio() {
//        return Optional.ofNullable(physicalStructure.getScalarByTag(xdSystemSideRatioTag))
//                .map(ScalarElement::getReal8)
//                .orElse(1.0);
//    }

//    public void setXdSystemSideRatio(double value) {
//        ScalarElement scalar = physicalStructure.getOrAddScalar(xdSystemSideRatioTag);
//        scalar.setTypeOfValue(PhysicalType.REAL8);
//        scalar.setReal8(value);
//    }
//
//    /**
//     * Gets the monitor-side part of the transducer ratio.
//     */
//    public double getXdMonitorSideRatio() {
//        return Optional.ofNullable(physicalStructure.getScalarByTag(xdMonitorSideRatioTag))
//                .map(ScalarElement::getReal8)
//                .orElse(1.0);
//    }
//
//    public void setXdMonitorSideRatio(double value) {
//        ScalarElement scalar = physicalStructure.getOrAddScalar(xdMonitorSideRatioTag);
//        scalar.setTypeOfValue(PhysicalType.REAL8);
//        scalar.setReal8(value);
//    }

    /**
     * Determines whether an element identified by the given tag exists in this object's physical structure.
     *
     * @param tag The tag of the element to search for.
     * @return True if the element exists; false otherwise.
     */
    public boolean hasElement(GUID tag) {
        return physicalStructure.getElementsByTag(tag).size() > 0;
    }
}

