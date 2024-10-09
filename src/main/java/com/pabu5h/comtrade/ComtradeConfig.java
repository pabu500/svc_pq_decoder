package com.pabu5h.comtrade;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ComtradeConfig {
    private String stationName;
    private String deviceId;
    private int revYear;
    private int numOfChannels;
    private int numOfAnalogChannels;
    private int numOfDigitalChannels;
    private List<Map<String, String>> analogChannels;
    private List<Map<String, String>> digitalChannels;
    private int lineFrequency;
    private List<int[]> sampleRates;
    private String startTimestamp;
    private String endTimestamp;
    private String fileType;
    private int timeMultiplier;
    private List<Double> analogValues;
    private List<Double> digitalValues;
}