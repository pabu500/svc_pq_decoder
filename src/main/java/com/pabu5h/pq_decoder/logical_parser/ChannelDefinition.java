package com.pabu5h.pq_decoder.logical_parser;

import com.pabu5h.pq_decoder.physical_parser.CollectionElement;
import com.pabu5h.pq_decoder.physical_parser.ScalarElement;
import com.pabu5h.pq_decoder.physical_parser.VectorElement;
import com.pabu5h.pq_decoder.util.GUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class ChannelDefinition {
    Logger logger = Logger.getLogger(ChannelDefinition.class.getName());
    private  CollectionElement physicalStructure;
    private  DataSourceRecord dataSource;
    private String channelName;
    private Phase phase;
    private GUID quantityTypeID;
    private QuantityMeasured quantityMeasured;
    private String quantityName;
//    private List<SeriesDefinition> seriesDefinitions;



    public static final GUID channelDefinitionIndexTag = new GUID("b48d858f-f5f5-11cf-9d89-0080c72e70a3");
    public static final GUID channelNameTag = new GUID("b48d8590-f5f5-11cf-9d89-0080c72e70a3");
    public static final GUID phaseIdTag = new GUID("b48d8591-f5f5-11cf-9d89-0080c72e70a3");
    public static final GUID quantityTypeIdTag = new GUID("b48d8592-f5f5-11cf-9d89-0080c72e70a3");
    public static final GUID quantityMeasuredIdTag = new GUID("c690e872-f755-11cf-9d89-0080c72e70a3");
    public static final GUID quantityNameTag = new GUID("b48d8595-f5f5-11cf-9d89-0080c72e70a3");
    public static final GUID seriesDefinitionsTag = new GUID("b48d8598-f5f5-11cf-9d89-0080c72e70a3");
    public static final GUID oneSeriesDefinitionTag = new GUID("b48d859a-f5f5-11cf-9d89-0080c72e70a3");

    public ChannelDefinition(CollectionElement physicalStructure, DataSourceRecord dataSource) {
        this.physicalStructure = physicalStructure;
        this.dataSource = dataSource;
    }

    public CollectionElement getPhysicalStructure() {
        return physicalStructure;
    }

    public DataSourceRecord getDataSource() {
        return dataSource;
    }

//    public String getChannelName() {
//        VectorElement vectorByTag = physicalStructure.getVectorByTag(CHANNEL_NAME_TAG);
//        if (vectorByTag == null) {
//            return null;
//        }
//        return new String(vectorByTag.getValues(), StandardCharsets.US_ASCII).trim('\0');
//    }

//    public void setChannelName(String value) {
//        byte[] bytes = (value + "\0").getBytes(StandardCharsets.US_ASCII);
//        physicalStructure.addOrUpdateVector(CHANNEL_NAME_TAG, PhysicalType.CHAR1, bytes);
//    }

//    public Phase getPhase() {
//        ScalarElement phaseElement = physicalStructure.getScalarByTag(PHASE_ID_TAG);
//        if (phaseElement == null) {
//            logger.info("Phase element not found in channel definition.");
//        }
//        return Phase.fromValue(phaseElement.getUInt4());
//    }

//    public void setPhase(Phase phase) {
//        ScalarElement scalar = physicalStructure.getOrAddScalar(PHASE_ID_TAG);
//        scalar.setTypeOfValue(PhysicalType.UNSIGNED_INTEGER4);
//        scalar.setUInt4((long) phase.getValue());
//    }

    public GUID getQuantityTypeID() {
        ScalarElement element = physicalStructure.getScalarByTag(quantityTypeIdTag);
        if (element == null) {
            logger.info("QuantityTypeID element not found in channel definition.");
        }
        return element.getTagOfElement();
    }

    // Static method to create a list of ChannelDefinitions from a CollectionElement
    public static List<ChannelDefinition> fromCollection(CollectionElement collectionElement) {
        // Assuming the collectionElement contains a list of elements, and we want to map them to ChannelDefinition objects
        return collectionElement.getElements().stream()
                .map(element -> new ChannelDefinition(collectionElement, null)) // Assuming `null` for dataSourceRecord or you can pass it
                .collect(Collectors.toList());
    }


//    public void setQuantityTypeID(GUID quantityTypeID) {
//        ScalarElement scalar = physicalStructure.getOrAddScalar(QUANTITY_TYPE_ID_TAG);
//        scalar.setTypeOfValue(PhysicalType.GUID);
//        scalar.setTagOfElement(quantityTypeID);
//    }

//    public QuantityMeasured getQuantityMeasured() {
//        ScalarElement element = physicalStructure.getScalarByTag(QUANTITY_MEASURED_ID_TAG);
//        if (element == null) {
//            throw new InvalidDataException("QuantityMeasured element not found in channel definition.");
//        }
//        return QuantityMeasured.fromValue((int) element.getUInt4());
//    }
//
//    public void setQuantityMeasured(QuantityMeasured quantityMeasured) {
//        ScalarElement scalar = physicalStructure.getOrAddScalar(QUANTITY_MEASURED_ID_TAG);
//        scalar.setTypeOfValue(PhysicalType.UNSIGNED_INTEGER4);
//        scalar.setUInt4((long) quantityMeasured.getValue());
//    }

//    public String getQuantityName() {
//        VectorElement vectorByTag = physicalStructure.getVectorByTag(QUANTITY_NAME_TAG);
//        if (vectorByTag == null) {
//            return null;
//        }
//        return new String(vectorByTag.getValues(), StandardCharsets.US_ASCII).trim('\0');
//    }

//    public void setQuantityName(String value) {
//        byte[] bytes = (value + "\0").getBytes(StandardCharsets.US_ASCII);
//        physicalStructure.addOrUpdateVector(QUANTITY_NAME_TAG, PhysicalType.CHAR1, bytes);
//    }
//
//    public List<SeriesDefinition> getSeriesDefinitions() {
//        CollectionElement collectionElement = physicalStructure.getCollectionByTag(SERIES_DEFINITIONS_TAG);
//        if (collectionElement == null) {
//            throw new InvalidDataException("SeriesDefinitions element not found in channel definition.");
//        }
//        List<SeriesDefinition> seriesDefinitions = new ArrayList<>();
//        for (CollectionElement collection : collectionElement.getElementsByTag(ONE_SERIES_DEFINITION_TAG)) {
//            seriesDefinitions.add(new SeriesDefinition(collection, this));
//        }
//        return seriesDefinitions;
//    }
//
//    public SeriesDefinition addNewSeriesDefinition() {
//        CollectionElement collectionElement = new CollectionElement();
//        collectionElement.setTagOfElement(ONE_SERIES_DEFINITION_TAG);
//        SeriesDefinition seriesDefinition = new SeriesDefinition(collectionElement, this);
//        collectionElement.setTagOfElement(SERIES_DEFINITIONS_TAG);
//
//        CollectionElement seriesCollection = physicalStructure.getCollectionByTag(SERIES_DEFINITIONS_TAG);
//        if (seriesCollection == null) {
//            seriesCollection = new CollectionElement();
//            seriesCollection.setTagOfElement(SERIES_DEFINITIONS_TAG);
//            physicalStructure.addElement(seriesCollection);
//        }
//        seriesCollection.addElement(collectionElement);
//
//        return seriesDefinition;
//    }
//
//    public void remove(SeriesDefinition seriesDefinition) {
//        CollectionElement collection = physicalStructure.getCollectionByTag(SERIES_DEFINITIONS_TAG);
//        if (collection == null) {
//            return;
//        }
//
//        for (CollectionElement item : collection.getElementsByTag(ONE_SERIES_DEFINITION_TAG)) {
//            SeriesDefinition def = new SeriesDefinition(item, this);
//            if (seriesDefinition.equals(def)) {
//                collection.removeElement(item);
//                break;
//            }
//        }
//    }
//
//    public static ChannelDefinition createChannelDefinition(DataSourceRecord dataSourceRecord) {
//        ChannelDefinition channelDefinition = dataSourceRecord.addNewChannelDefinition();
//        channelDefinition.setPhase(Phase.NONE);
//        channelDefinition.setQuantityMeasured(QuantityMeasured.NONE);
//        channelDefinition.getPhysicalStructure().addElement(new CollectionElement(SERIES_DEFINITIONS_TAG));
//        return channelDefinition;
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ChannelDefinition) {
            ChannelDefinition other = (ChannelDefinition) obj;
            return physicalStructure.equals(other.physicalStructure);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return physicalStructure.hashCode();
    }

//    @Override
//    public int compareTo(ChannelDefinition other) {
//        return physicalStructure.compareTo(other.physicalStructure);
//    }
}