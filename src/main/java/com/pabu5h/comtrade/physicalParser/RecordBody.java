package com.pabu5h.comtrade.physicalParser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
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


