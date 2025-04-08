package com.pabu5h.pq_decoder;

import com.pabu5h.pq_decoder.logical_parser.ChannelInstance;
import com.pabu5h.pq_decoder.logical_parser.LogicalParser;
import com.pabu5h.pq_decoder.logical_parser.ObservationRecord;
import com.pabu5h.pq_decoder.logical_parser.SeriesInstance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PqdModule {
    public Map<String, Object> extractLogicalData(String filePath, String step) throws Exception {
        Logger logger = Logger.getLogger(PqdModule.class.getName());
        Map<String, Object> channelInfoMap = new HashMap<>();

        LogicalParser parserLogical = new LogicalParser(filePath);
        parserLogical.open();
        List<Map<String, Object>> allRecords = new ArrayList<>();

        while (parserLogical.hasNextObservationRecord()) {
            ObservationRecord record = parserLogical.nextObservationRecord();
            String dataSourceName = Optional.ofNullable(record.getDataSource().dataSourceName).orElse("");
            String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getCreateTime());
            int channelCount = record.getChannelInstances().size();
            double nominalFrequency = record.getSettings().getNominalFrequency();

            String norminalFrequency = String.valueOf(record.getSettings().getNominalFrequency());
            int firstNegativeValueIndex = 0;
            int nextNegativeValueIndex = 0;
            int firstIndexFound = 0;
            int secondIndexFound = 0;
            int samplingRate=0;
            boolean foundFirst = false; // Flag to track if we found the firstNegativeValueIndex
            boolean foundFirst2 = false;
            boolean firstChannel = true;
            List<Map<String, Object>> channelValues = new ArrayList<>();
            Boolean isInterruption = false;

            for (ChannelInstance channelInstance : record.getChannelInstances()) {
                String channelName = channelInstance.getDefinition().getChannelName();
                String phase = channelInstance.getDefinition().getPhase().toString();
                String measurementUnit = channelInstance.getDefinition().getQuantityMeasured().toString();

                if (!channelInstance.getSeriesInstances().isEmpty()) {
                    SeriesInstance firstSeriesInstance = channelInstance.getSeriesInstances().get(1);
                    List<Double> extractedValues = new ArrayList<>();

                    if (firstSeriesInstance.getOriginalValues() instanceof List<?>) {
                        List<Double> values = ((List<?>) firstSeriesInstance.getOriginalValues()).stream()
                                .filter(BigDecimal.class::isInstance)  // Filter for BigDecimal instances
                                .map(BigDecimal.class::cast)           // Cast to BigDecimal
                                .map(BigDecimal::doubleValue)         // Convert BigDecimal to Double
                                .collect(Collectors.toList());


                        for (int i = 0; i < values.size() - 1; i++) {
                            if (values.get(i) < 0 && values.get(i + 1) >= 0) {
                                if (!foundFirst) {
                                    firstIndexFound = i;
                                    foundFirst = true;
                                } else {
                                    secondIndexFound = i;
                                    break;
                                }
                            }
                        }

                        if (secondIndexFound - firstIndexFound < 60) {
                            isInterruption = true;
                        }

                        if(!isInterruption){
                            for(int i=0; i<values.size()-1; i++){
                                if(values.get(i) < 0 && values.get(i+1) >= 0 && firstChannel){
                                    if(!foundFirst2){
                                        firstNegativeValueIndex = i;
                                        foundFirst2 = true;
                                    }else{
                                        nextNegativeValueIndex = i;
                                        samplingRate = nextNegativeValueIndex - firstNegativeValueIndex;
                                        logger.info("First negative value index: " + firstNegativeValueIndex);
                                        logger.info("Next negative value index: " + nextNegativeValueIndex);
                                        logger.info("Sampling rate: " + samplingRate);
                                        break;
                                    }
                                }
                            }
                        }else{
                            for(int i=values.size()-1;i>=1;i--){
                                if(values.get(i) < 0 && values.get(i-1) >= 0 && firstChannel){
                                    if(!foundFirst2){
                                        firstNegativeValueIndex = i;
                                        foundFirst2 = true;
                                    }else{
                                        nextNegativeValueIndex = i;
                                        samplingRate = firstNegativeValueIndex - nextNegativeValueIndex;
                                        logger.info("First negative value index: " + firstNegativeValueIndex);
                                        logger.info("Next negative value index: " + nextNegativeValueIndex);
                                        logger.info("Sampling rate: " + samplingRate);
                                        break;
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < values.size(); i += Integer.parseInt(step)) {
                            extractedValues.add(values.get(i));
                        }

                        firstChannel = false;

                        Map<String, Object> channelData = Map.of(
                                "channel_name", channelName,
                                "phase", phase,
                                "measurement_unit", measurementUnit,
                                "values", extractedValues
                        );
                        channelValues.add(channelData);
                    }
                }
            }
            Map<String, Object> recordData = Map.of(
                    "file_name", dataSourceName,
                    "timestamp", startTime,
                    "channel_count", channelCount,
                    "sampling_rate", samplingRate,
                    "channel_info", channelValues
            );
            allRecords.add(recordData);
        }
        channelInfoMap.put("data",Map.of("pqd_data",Map.of("logical_parser", allRecords)) );
        parserLogical.Close();
        logger.info("Logical parser processed successfully");
        return channelInfoMap;
    }
}
