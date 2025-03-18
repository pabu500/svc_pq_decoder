package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.Element;
import com.pabu5h.pq_decoder.physical_parser.Record;
import com.pabu5h.pq_decoder.physical_parser.RecordType;
import com.pabu5h.pq_decoder.util.GUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Component
@Getter
@Setter
public class DataSourceRecord {
    Logger logger = Logger.getLogger(LogicalParser.class.getName());
    @Autowired
    private Record physicalRecord;
    @Autowired
    private CollectionElement collectionElement;
    @Autowired
    private CollectionElement collectionElement2;
    @Autowired
    private CollectionElement collectionElementSub;

    @Autowired
    private Element element;
    private String sourceId;
    private String sourceName;

    public static GUID DataSourceTypeIDTag = new GUID("b48d8581-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID VendorIDTag = new GUID("b48d8582-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID EquipmentIDTag = new GUID("b48d8583-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID DataSourceNameTag = new GUID("b48d8587-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID DataSourceOwnerTag = new GUID("b48d8588-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID DataSourceLocationTag = new GUID("b48d8589-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID DataSourceCoordinatesTag = new GUID("b48d858b-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID ChannelDefinitionsTag = new GUID("b48d858d-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID OneChannelDefinitionTag = new GUID("b48d858e-f5f5-11cf-9d89-0080c72e70a3");
    public static GUID EffectiveTag = new GUID("62f28183-f9c4-11cf-9d89-0080c72e70a3");

    public DataSourceRecord(Record physicalRecord) {
        this.physicalRecord = physicalRecord;
    }

    public ChannelDefinition addNewChannelDefinition() {
        collectionElement.setTagOfElement(OneChannelDefinitionTag);
        ChannelDefinition channelDefinition = new ChannelDefinition(collectionElement,this);
        channelDefinition.setPhase(Phase.NONE);
        channelDefinition.setQuantityMeasured(QuantityMeasured.NONE);
        collectionElementSub.setTagOfElement(ChannelDefinition.seriesDefinitionsTag);
        collectionElement.addElement(collectionElementSub);
        collectionElement2 = this.physicalRecord.getBody().getCollection().getCollectionByTag(ChannelDefinitionsTag);
        if(collectionElement2 == null) {
            collectionElement2 = new CollectionElement();
            collectionElement2.setTagOfElement(ChannelDefinitionsTag);
            this.physicalRecord.getBody().getCollection().addElement(collectionElement2);
        }
        collectionElement2.addElement(collectionElement);
        return channelDefinition;
    }


//    public UUID getDataSourceTypeID() {
//        ScalarElement scalarElement = physicalRecord.getBody().getCollection().getScalarByTag(DataSourceTypeIDTag);
//        if (scalarElement == null) {
//            throw new InvalidDataException("DataSourceTypeID element not found in data source record.");
//        }
//        return scalarElement.getGuid();
//    }
//
//    public void setDataSourceTypeID(UUID value) {
//        ScalarElement scalar = physicalRecord.getBody().getCollection().getOrAddScalar(DataSourceTypeIDTag);
//        scalar.setGuid(value);
//    }
//
//    public String getDataSourceName() {
//        VectorElement vectorElement = physicalRecord.getBody().getCollection().getVectorByTag(DataSourceNameTag);
//        if (vectorElement == null) {
//            throw new InvalidDataException("DataSourceName element not found in data source record.");
//        }
//        return new String(vectorElement.getValues(), StandardCharsets.US_ASCII).trim();
//    }
//
//    public void setDataSourceName(String value) {
//        physicalRecord.getBody().getCollection().addOrUpdateVector(
//                value.getBytes(StandardCharsets.US_ASCII),
//                DataSourceNameTag
//        );
//    }
//
//    // Other getter and setter methods as needed...
//
//    public static final UUID DataSourceTypeIDTag = UUID.fromString("b48d8581-f5f5-11cf-9d89-0080c72e70a3");
//    public static final UUID DataSourceNameTag = UUID.fromString("b48d8587-f5f5-11cf-9d89-0080c72e70a3");
//
//    // Create a new DataSourceRecord
//    public static DataSourceRecord createDataSourceRecord(String dataSourceName) {
//        Record record = new Record(RecordType.DATA_SOURCE);
//        DataSourceRecord dataSourceRecord = new DataSourceRecord(record);
//        dataSourceRecord.setDataSourceTypeID(DataSourceType.SIMULATE);
//        dataSourceRecord.setDataSourceName(dataSourceName);
//        record.getBody().getCollection().addElement(new CollectionElement(DataSourceRecord.DataSourceTypeIDTag));
//        return dataSourceRecord;
//    }
//
    // Channel Definitions handling
public List<ChannelDefinition> getChannelDefinitions() {
    // Get the collection using DataSourceTypeIDTag
    CollectionElement collection = (CollectionElement) physicalRecord.getBody().getCollection()
            .getCollectionByTag(DataSourceRecord.DataSourceTypeIDTag);

    // Check if collection is null and log or throw exception
    if (collection == null) {
        String errorMessage = "ChannelDefinitions element not found in data source record.";
        logger.severe(errorMessage);
    }
    // Assuming ChannelDefinition.fromCollection returns a List<ChannelDefinition>
    assert collection != null;
    return ChannelDefinition.fromCollection(collection);
}



    // <summary>
    // Creates a new data source record from the given physical record
    // if the physical record is of type data source. Returns null if it is not.
    // </summary>
    // <param name="physicalRecord">The physical record used to create the data source record.</param>
    // <returns>The new data source record, or null if the physical record does not define a data source record.</returns>
    public static DataSourceRecord createDataSourceRecord(Record physicalRecord) {
        if (physicalRecord.getHeader().getTypeOfRecord() != RecordType.DataSource) {
            return null;
        }

        return new DataSourceRecord(physicalRecord);
    }



}
