package com.pabu5h.pq_decoder.physical_parser;

import java.util.UUID;

public abstract class Element {
    private UUID tagOfElement;
    private PhysicalType typeOfValue;

    // Getter and Setter for TagOfElement
    public UUID getTagOfElement() {
        return tagOfElement;
    }

    public void setTagOfElement(UUID tagOfElement) {
        this.tagOfElement = tagOfElement;
    }

    // Abstract method to be implemented by subclasses
    public abstract ElementType getTypeOfElement();

    // Getter and Setter for TypeOfValue
    public PhysicalType getTypeOfValue() {
        return typeOfValue;
    }

    public void setTypeOfValue(PhysicalType typeOfValue) {
        this.typeOfValue = typeOfValue;
    }
}

