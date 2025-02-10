package com.pabu5h.pq_decoder.physical_parser;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Record {
    private final RecordHeader header;
    private final RecordBody body;

    public Record(RecordHeader header, RecordBody body) {
        this.header = header;
        this.body = body;
    }
}

