package com.pabu5h.pq_decoder.physical_parser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pabu5h.pq_decoder.util.GUID;
import com.pabu5h.pq_decoder.util.GUID.GUIDSerializer;

public abstract class Element {
	
	@JsonSerialize(using = GUIDSerializer.class)
    private GUID tagOfElement = GUID.Empty;
    private PhysicalType typeOfValue;

    // Getter and Setter for TagOfElement
    @JsonSerialize(using = GUIDSerializer.class)
    public GUID getTagOfElement() {
        return tagOfElement;
    }

    public void setTagOfElement(GUID tagOfElement) {
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [Tag=" + (tagOfElement == null ? null : tagOfElement.toString()) + ", Type=" + getTypeOfValue() + "]";
	}
    
    
}

