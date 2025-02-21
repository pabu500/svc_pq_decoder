package com.pabu5h.pq_decoder.physical_parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecordBody {

    private CollectionElement collection;
    private long checksum;

    // Constructor that accepts a rootTag (UUID)
    public RecordBody(CollectionElement element) {
        this.collection = element;
    }

//    public RecordBody(int size) {
//        this.collection = new CollectionElement(size);
//    }
}


