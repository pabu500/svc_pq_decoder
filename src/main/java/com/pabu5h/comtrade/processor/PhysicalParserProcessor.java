package com.pabu5h.comtrade.processor;

import com.pabu5h.comtrade.physicalParser.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PhysicalParserProcessor{
    public PhysicalParser readPqdifFile(String filePath){
        return new PhysicalParser(filePath);
    }




}
