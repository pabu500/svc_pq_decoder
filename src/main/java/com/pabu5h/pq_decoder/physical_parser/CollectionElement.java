package com.pabu5h.pq_decoder.physical_parser;
import java.util.List;
import java.util.ArrayList;


import lombok.*;
import java.util.*;

@Data
public class CollectionElement extends Element {
    private final List<Element> elements;
    private int readSize;

    // The size of the collection
    public int size() {
        return elements.size();
    }

    // The read size from the file
    public int getReadSize() {
        return readSize;
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }

    // Element type of the collection
    @Override
    public ElementType getTypeOfElement() {
        return ElementType.COLLECTION;
    }

    // create a collection element with size of the num that is passed in
    public CollectionElement(int size) {
        this.elements = new ArrayList<>(size);  // Initialize the list of elements
        this.readSize = size;
    }

    // Constructor to initialize the collection without rootTag (if needed)
    public CollectionElement() {
        this.elements = new ArrayList<>();
    }

    // Add an element to the collection
    public void addElement(Element element) {
        elements.add(element);
    }

    // Remove an element from the collection
    public void removeElement(Element element) {
        elements.remove(element);
    }

    // Remove elements by tag
    public void removeElementsByTag(UUID tag) {
        elements.removeIf(element -> element.getTagOfElement().equals(tag));
    }

    // Get elements by tag
    public List<Element> getElementsByTag(UUID tag) {
        List<Element> result = new ArrayList<>();
        for (Element element : elements) {
            if (element.getTagOfElement().equals(tag)) {
                result.add(element);
            }
        }
        return result;
    }

    // Get a collection by tag
    public CollectionElement getCollectionByTag(UUID tag) {
        for (Element element : elements) {
            if (element.getTagOfElement().equals(tag)) {
                return (CollectionElement) element;
            }
        }
        return null;
    }

    // Get a scalar element by tag
    public ScalarElement getScalarByTag(UUID tag) {
        for (Element element : elements) {
            if (element.getTagOfElement().equals(tag)) {
                return (ScalarElement) element;
            }
        }
        return null;
    }

    // Get a vector element by tag
    public VectorElement getVectorByTag(UUID tag) {
        for (Element element : elements) {
            if (element.getTagOfElement().equals(tag)) {
                return (VectorElement) element;
            }
        }
        return null;
    }

    // Get or add scalar element
    public ScalarElement getOrAddScalar(UUID tag) {
        ScalarElement scalarElement = getScalarByTag(tag);
        if (scalarElement == null) {
            scalarElement = new ScalarElement();
            scalarElement.setTagOfElement(tag);
            addElement(scalarElement);
        }
        return scalarElement;
    }

    // Get or add vector element
    public VectorElement getOrAddVector(UUID tag) {
        VectorElement vectorElement = getVectorByTag(tag);
        if (vectorElement == null) {
            vectorElement = new VectorElement();
            vectorElement.setTagOfElement(tag);
            addElement(vectorElement);
        }
        return vectorElement;
    }

    // Add or update scalar element
    public ScalarElement addOrUpdateScalar(UUID tag, PhysicalType type, byte[] bytes) {
        ScalarElement scalarElement = getOrAddScalar(tag);
        scalarElement.setTypeOfValue(type);
        // scalarElement.setValue(bytes, 0); // Uncomment and use this if necessary
        return scalarElement;
    }

    // Add or update vector element
    public VectorElement addOrUpdateVector(UUID tag, PhysicalType type, byte[] bytes) {
        VectorElement vectorElement = getOrAddVector(tag);
        vectorElement.setTypeOfValue(type);
        vectorElement.setSize(bytes.length / type.getByteSize());
        // vectorElement.setValues(bytes, 0); // Uncomment and use this if necessary
        return vectorElement;
    }

    // To string method for displaying collection details
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Collection -- Size: %d, Tag: %s", size(), getTagOfElement()));
        for (Element element : elements) {
            String[] lines = element != null ? element.toString().split(System.lineSeparator()) : new String[0];
            for (String line : lines) {
                stringBuilder.append(System.lineSeparator()).append("    ").append(line);
            }
        }
        return stringBuilder.toString();
    }

}


