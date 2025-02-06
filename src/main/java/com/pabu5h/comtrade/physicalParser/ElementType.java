package com.pabu5h.comtrade.physicalParser;

import lombok.Getter;

@Getter
public enum ElementType {
    /**
     * Collection element.
     * Represents a collection of elements.
     */
    COLLECTION((byte) 1),

    /**
     * Scalar element.
     * Represents a single value.
     */
    SCALAR((byte) 2),

    /**
     * Vector element.
     * Represents a collection of values.
     */
    VECTOR((byte) 3);

    // Getter to retrieve the value of the enum constant
    private final byte value;

    // Constructor to set the value of the enum constant
    ElementType(byte value) {
        this.value = value;
    }

    // Static method to get the enum constant from its byte value
    public static ElementType fromValue(byte value) {
        for (ElementType type : ElementType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}


