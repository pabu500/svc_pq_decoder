//package com.pabu5h.pq_decoder.util;
//
//import com.pabu5h.pq_decoder.physical_parser.Element;
//import com.pabu5h.pq_decoder.physical_parser.ElementType;
//import com.pabu5h.pq_decoder.physical_parser.PhysicalType;
//
//import java.util.UUID;
//
//public class ErrorElement extends Element {
//    private ElementType elementType;
//    private Exception ex;
//    private UUID tagOfElement;
//    private PhysicalType typeOfValue;
//
//    // Constructor accepting defaults for elementType, tagOfElement, and typeOfValue
//    public ErrorElement(ElementType elementType, Exception ex) {
//        this.elementType = elementType != null ? elementType : ElementType.Unknown;  // Default to Unknown
//        this.ex = ex;
//        this.tagOfElement = new UUID(0, 0);  // Default UUID (empty)
//        this.typeOfValue = PhysicalType.Scalar;  // Default PhysicalType
//    }
//
//    // Setters
//    public ErrorElement setTagOfElement(UUID tagOfElement) {
//        this.tagOfElement = tagOfElement;
//        return this;
//    }
//
//    public ErrorElement setTypeOfValue(PhysicalType typeOfValue) {
//        this.typeOfValue = typeOfValue;
//        return this;
//    }
//}
//
