package com.pabu5h.comtrade.config;

import lombok.Data;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class ComtradeConfig {
    private String stationName;
    private String deviceId;
    private int revYear;
    private int numOfChannels;
    private int numOfAnalogChannels;
    private int numOfDigitalChannels;
    private double lineFrequency;
    private int sampleRates;
    private int numOfSamples;
    private String startTimestamp;
    private String endTimestamp;
    private String fileType;
    private int timeMultiplier;
    private List<LinkedHashMap<String, Object>> analogChannels;
    private List<LinkedHashMap<String, Object>> digitalChannels;
}