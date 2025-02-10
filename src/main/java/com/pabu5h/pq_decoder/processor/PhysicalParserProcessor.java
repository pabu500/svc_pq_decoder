package com.pabu5h.pq_decoder.processor;

import com.pabu5h.pq_decoder.physical_parser.*;
import org.springframework.stereotype.Service;

@Service
public class PhysicalParserProcessor{
    public PhysicalParser readPqdifFile(String filePath){
        return new PhysicalParser(filePath);
    }




}
