package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.logical_parser.*;
import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
import com.pabu5h.pq_decoder.util.GUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class MonitorSettingRecord {
    Logger logger = Logger.getLogger(ChannelDefinition.class.getName());
    private  CollectionElement collectionElement;
    private DataSourceRecord dataSource;
    private String channelName;
    private Phase phase;
    private GUID quantityTypeID;
    private QuantityMeasured quantityMeasured;
    private String quantityName;
    public Record physicalRecord;

    private CollectionElement collectionElement2;

    public static final GUID dataSourceTypeIdTag = new GUID("b48d8581-f5f5-11cf-9d89-0080c72e70a3");
    public static final GUID effectiveTag = new GUID("62f28183-f9c4-11cf-9d89-0080c72e70a3");
    public static final GUID timeInstalledTag = new GUID("3d786f85-f76e-11cf-9d89-0080c72e70a3");
    public static final GUID useCalibrationTag = new GUID("62f28180-f9c4-11cf-9d89-0080c72e70a3");
    public static final GUID useTransducerTag = new GUID("62f28181-f9c4-11cf-9d89-0080c72e70a3");
    public static final GUID channelSettingsArrayTag = new GUID("62f28182-f9c4-11cf-9d89-0080c72e70a3");
    public static final GUID oneChannelSettingTag = new GUID("3d786f9a-f76e-11cf-9d89-0080c72e70a3");
    public static final GUID nominalFrequencyTag = new GUID("0fa118c3-cb4a-11d2-b30b-fe25cb9a1760");


    private MonitorSettingRecord(Record physicalRecord) {
        this.physicalRecord = physicalRecord;
    }

    public ChannelSetting addNewChannelDefinition(ChannelDefinition channelDefinition) {
        collectionElement.setTagOfElement(oneChannelSettingTag);
        ChannelSetting result = new ChannelSetting(collectionElement,this);
        result.setChannelDefinitionIndex(channelDefinition.getDataSource().getChannelDefinitions().indexOf(channelDefinition));

        collectionElement2 = this.physicalRecord.getBody().getCollection().getCollectionByTag(channelSettingsArrayTag);
        if(collectionElement2 == null) {
            collectionElement2 = new CollectionElement();
            collectionElement2.setTagOfElement(oneChannelSettingTag);
            this.physicalRecord.getBody().getCollection().addElement(collectionElement2);
        }
        collectionElement2.addElement(collectionElement);
        return result;
    }

    public static MonitorSettingRecord createMonitorSettingRecord (Record physicalRecord) {
        if (physicalRecord.getHeader().getTypeOfRecord() != RecordType.MonitorSettings) {
            return null;
        }

        return new MonitorSettingRecord(physicalRecord);
    }

}
