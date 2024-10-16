package com.pabu5h.comtrade;
import com.pabu5h.comtrade.config.ComtradeConfig;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ComtradeModule {
    Logger logger = Logger.getLogger(ComtradeModule.class.getName());
    private ComtradeConfig comtradeConfig;

    private byte[] datByteData;

    public Map<String,Object> processCfgConfig(InputStream cfgInputStream, InputStream datInputStream,int step) throws IOException {
        comtradeConfig = new ComtradeConfig();
        Map<String,Object> response = new HashMap<>();
        // Reads the configuration file
        try (BufferedReader cfgReader = new BufferedReader(new InputStreamReader(cfgInputStream))) {
            // Read the first three fields (Station, Device, Year), e.g. Phase 5 CT Lab Test,Mon000004053,1999
            String[] header = cfgReader.readLine().split(",");
            comtradeConfig.setStationName(header[0]);
            comtradeConfig.setDeviceId(header[1]);
            comtradeConfig.setRevYear(Integer.parseInt(header[2]));

            // Read number of channels,analog and digital channels respectively, e.g 8,8A,0D
            String[] channelInfo = cfgReader.readLine().split(",");
            comtradeConfig.setNumOfChannels(Integer.parseInt(channelInfo[0]));
            comtradeConfig.setNumOfAnalogChannels(Integer.parseInt(channelInfo[1].replaceAll("A", "")));
            comtradeConfig.setNumOfDigitalChannels(Integer.parseInt(channelInfo[2].replaceAll("D", "")));

            // Read analog channels attributes
            List<LinkedHashMap<String, Object>> analogChannels = new ArrayList<>();
            for (int i = 0; i < comtradeConfig.getNumOfAnalogChannels(); i++) {
                String[] analogLine = cfgReader.readLine().split(",");
                LinkedHashMap<String, Object> Channel = new LinkedHashMap<>();
                Channel.put("channel_num", analogLine[0]);
                Channel.put("name", analogLine[1]);
                Channel.put("phase_id", analogLine[2]);
                Channel.put("component_id", analogLine[3]);
                Channel.put("unit", analogLine[4]);
                Channel.put("multiplier", analogLine[5]);
                Channel.put("offset", analogLine[6]);
                Channel.put("time_skew", analogLine[7]);
                Channel.put("min_value", analogLine[8]);
                Channel.put("max_value", analogLine[9]);
                Channel.put("primary_ratio", analogLine[10]);
                Channel.put("secondary_ratio", analogLine[11]);
                Channel.put("pri_sec_source", analogLine[12]);
                analogChannels.add(Channel);
            }
            comtradeConfig.setAnalogChannels(analogChannels);

            // Read analog channels attributes
            List<LinkedHashMap<String, Object>> digitalChannels = new ArrayList<>();
            for (int i = 0; i < comtradeConfig.getNumOfDigitalChannels(); i++) {
                String[] analogLine = cfgReader.readLine().split(",");
                LinkedHashMap<String, Object> Channel = new LinkedHashMap<>();
                Channel.put("channel_num", analogLine[0]);
                Channel.put("name", analogLine[1]);
                Channel.put("phase_id", analogLine[2]);
                Channel.put("component_id", analogLine[3]);
                Channel.put("state", analogLine[4]);
                digitalChannels.add(Channel);
            }
            comtradeConfig.setDigitalChannels(digitalChannels);

            // Read line frequency
            comtradeConfig.setLineFrequency(Double.parseDouble(cfgReader.readLine()));

            // Read sample rate
            int numSampleRates = Integer.parseInt(cfgReader.readLine());
            for (int i = 0; i < numSampleRates; i++) {
                String[] sampleRateInfo = cfgReader.readLine().split(",");
                comtradeConfig.setSampleRates(Integer.parseInt(sampleRateInfo[0]));
                comtradeConfig.setNumOfSamples(Integer.parseInt(sampleRateInfo[1]));
            }

            // Read start and end timestamps
            comtradeConfig.setStartTimestamp(cfgReader.readLine());
            comtradeConfig.setEndTimestamp(cfgReader.readLine());

            // Read file type (ASCII or BINARY)
            comtradeConfig.setFileType(cfgReader.readLine());

            // Read time multiplier
            comtradeConfig.setTimeMultiplier(Integer.parseInt(cfgReader.readLine()));
        }

        datByteData = datInputStream.readAllBytes(); // This reads the entire stream into a byte array
        // Process data file based on type
        Map<String,Object> result = new HashMap<>();
        if(comtradeConfig.getNumOfAnalogChannels()>0 || comtradeConfig.getNumOfDigitalChannels()>0){
            if ("BINARY".equalsIgnoreCase(comtradeConfig.getFileType())) {
                result =  readBinaryData(datByteData,step);
            } else if ("ASCII".equalsIgnoreCase(comtradeConfig.getFileType())) {
                result =  readAsciiData(datByteData);
            }else{
                result.put("error","Invalid file type");
            }
        }
        response.put("result",comtradeConfig);
        Map<String,Object> error = (Map<String, Object>) result.get("error");
        if(error!=null){
            response.put("error",result.get("error"));
        }
        return response;
    }

    public Map<String,Object> readBinaryData(byte[] datByteData,int step) {
        // Getting auxiliary variables
        int numOfAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
        int numOfDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
        int numOfDigitalBytes = (int) Math.ceil(numOfDigitalChannels / 16.0);
        int numberOfSamples = comtradeConfig.getNumOfSamples();

        // Check for valid number of samples
        if (numberOfSamples <= 0) {
            logger.info("Error: Number of samples must be greater than 0.");
            return Map.of("error","Number of samples must be greater than 0.");
        }

        //32 + 32 + 16*nA + 16*nD
        int bytesPerSample = 4 + 4 + numOfAnalogChannels * 2 + numOfDigitalBytes * 2;
        Map<String, Object> errorMap = new HashMap<>();

        for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex+=step) {
            int startIndex = sampleIndex * bytesPerSample;
            int endIndex = startIndex + bytesPerSample;

            // Validate startIndex and endIndex to prevent IndexOutOfBoundsException
            if (endIndex > datByteData.length) {
                logger.warning("Error: Not enough data in datByteData for sample index: " + sampleIndex);
                break;
            }

            byte[] sampleData = Arrays.copyOfRange(datByteData, startIndex, endIndex);
            int[] unpackedData;

            try {
                unpackedData = unpackSampleData(sampleData, numOfAnalogChannels, numOfDigitalBytes);
            } catch (Exception e) {
                logger.severe("Error: Unable to unpack sample data for sample index: " + sampleIndex + " , " + e.getMessage());
                errorMap.put("sample index " + sampleIndex, "Error: Unable to unpack sample data for sample index: " + sampleIndex + " , " + e.getMessage());
                continue; // Skip this sample and move to the next one
            }

            // Process analog channels
            for (int i = 0; i < comtradeConfig.getAnalogChannels().size(); i++) {
                try {
                    int value = unpackedData[i + 2]; // Adjust according to the unpacked data format
                    double multiplier = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("multiplier"));
                    double offset = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("offset"));
                    double adjustedValue = value * multiplier + offset;

                    // Get the list of values for the current analog channel
                    List<Double> valuesList = (List<Double>) comtradeConfig.getAnalogChannels().get(i).get("values");

                    // If the list doesn't exist, create a new one and store it
                    if (valuesList == null) {
                        valuesList = new ArrayList<>();
                        comtradeConfig.getAnalogChannels().get(i).put("values", valuesList);
                    }

                    // Append the adjusted value to the list
                    valuesList.add(adjustedValue);
                } catch (Exception e) {
                    logger.severe("Error: Unable to process analog channel data for sample index: " + sampleIndex + " , " + e.getMessage());
                    errorMap.put("analog channel " + i, "Error: Unable to process analog channel data for sample index: " + sampleIndex + " , " + e.getMessage());
                }
            }

            // Process digital channels
            for (int j = 0; j < comtradeConfig.getDigitalChannels().size(); j++) {
                try {
                    int digitalValue = unpackedData[numOfAnalogChannels + 2]; // Fixed index for digital value
                    int bitPosition = j % 16;
                    int mask = (1 << bitPosition);
                    double data = (digitalValue & mask) != 0 ? 1.0 : 0.0; // Convert to 1.0 or 0.0

                    // Get the list of values for the current digital channel
                    List<Double> digitalValuesList = (List<Double>) comtradeConfig.getDigitalChannels().get(j).get("values");

                    // If the list doesn't exist, create a new one and store it
                    if (digitalValuesList == null) {
                        digitalValuesList = new ArrayList<>();
                        comtradeConfig.getDigitalChannels().get(j).put("values", digitalValuesList);
                    }

                    // Append the digital value to the list
                    digitalValuesList.add(data);
                } catch (Exception e) {
                    logger.severe("Error: Unable to process digital channel data for sample index: " + sampleIndex + " , " + e.getMessage());
                    errorMap.put("digital channel " + j, "Error: Unable to process digital channel data for sample index: " + sampleIndex + " , " + e.getMessage());
                }
            }
        }

        if (!errorMap.isEmpty()) {
            return Map.of("error", errorMap);
        }
        return Map.of("success", true);
    }

    private int[] unpackSampleData(byte[] sampleData, int numOfAnalogShorts, int numOfHeaderShorts) {
        ByteBuffer buffer = ByteBuffer.wrap(sampleData);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Assuming little-endian based on the Python example

        // metadata values
        // Read the two integers (4 bytes each)
        int int1 = buffer.getInt();
        int int2 = buffer.getInt();

        // Read the short values for analog channels (2 bytes each)
        int[] analogShortValues = new int[numOfAnalogShorts];
        for (int i = 0; i < numOfAnalogShorts; i++) {
            // reads the next two bytes from the buffer and converts them into a short value
            analogShortValues[i] = buffer.getShort();
        }

        // Read the short values for header bytes / digital channels (2 bytes each)
        int[] headerShortValues = new int[numOfHeaderShorts];
        for (int i = 0; i < numOfHeaderShorts; i++) {
            headerShortValues[i] = buffer.getShort(); // If unsigned, handle conversion appropriately
        }

        // Combine all values into one array e.g result[10] = {int1, int2, short1, short2, ...}
        int[] result = new int[2 + numOfAnalogShorts  + numOfHeaderShorts];
        //append metadata values
        result[0] = int1;
        result[1] = int2;
        //append analog values
        System.arraycopy(analogShortValues, 0, result, 2, numOfAnalogShorts);
        //append header values
        System.arraycopy(headerShortValues, 0, result, 2 + numOfAnalogShorts, numOfHeaderShorts);

        return result;
    }


    public Map<String,Object> readAsciiData(byte[] datByteData) throws IOException {
        String content = new String(datByteData, StandardCharsets.UTF_8);
        List<String> lines = Arrays.asList(content.split("\n"));
        Map<String,Object> errorMap = new HashMap<>();
        List<List<Double>> data = lines.stream()
                .skip(1) // Skip the header
                .map(line -> Arrays.stream(line.split(","))
                        .map(Double::parseDouble)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        int nAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
        for (int i = 0; i < nAnalogChannels; i++) {
            final int index = i; // Create a final variable to use in the lambda expression
            List<Double> values = data.stream()
                    .map(row -> row.get(index))
                    .toList();
            if (comtradeConfig.getAnalogChannels().size() > i) {
                double multiplier = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("multiplier"));
                double offSet = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("offset"));
                List<Double> adjustedValues = values.stream().map(value -> value * multiplier + offSet).toList();
                comtradeConfig.getAnalogChannels().get(index).put("values", adjustedValues);
            } else {
                logger.warning("Index out of bounds for analog channels: " + i);
                errorMap.put("analog channel "+i,"Index out of bounds for analog channels: " + i);
            }
        }

        data = data.stream().map(row -> row.subList(nAnalogChannels, row.size())).toList();

        int nDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
        for (int i = 0; i < nDigitalChannels; i++) { // Corrected here
            final int index = i;
            List<Double> values = data.stream()
                    .map(row -> row.get(index))
                    .collect(Collectors.toList());
            comtradeConfig.getDigitalChannels().get(i).put("values", values);
        }
        if(!errorMap.isEmpty()){
            return Map.of("error",errorMap);
        }
        return Map.of("success",true);
    }




//    private Map<String,Object> readBinaryAnalog(byte[] datByteData) {
//        //Getting auxiliary variables
//        int numOfAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
//        int numOfDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
//        int numOfDigitalBytes = (int) Math.ceil(numOfDigitalChannels / 16.0);
//        int numberOfSamples = comtradeConfig.getNumOfSamples();
//        // Check for valid number of samples
//        if (numberOfSamples <= 0) {
//            System.err.println("Error: Number of samples must be greater than 0.");
//            return Map.of("error","Number of samples must be greater than 0.");
//        }
//
//        //4 bytes for the first value (32 bits)
//        //4 bytes for the second value. (32 bits)
//        //2 bytes for each analog channel. (16 bits)
//        //2 bytes for each header byte. (16 bits)
//        int bytesPerSample = 4 + 4 + numOfAnalogChannels * 2 + numOfDigitalBytes * 2;
//        HashMap<String, Object> errors = new HashMap<>();
//        for (int i = 0; i < numOfAnalogChannels; i++) {
//            List<Double> values = new ArrayList<>();
//            for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++) {
//                // nbps = 4+4+nA*2+nH*2
//                // data = self.bdata[sampleIndex*nbps:(sampleIndex+1)*nbps]
//                int startIndex = sampleIndex * bytesPerSample; //e.g 2 * 108 (start index to read)
//                int endIndex = startIndex + bytesPerSample;   // 2*108 + 108 (end index to read)
//
//                // Validate startIndex and endIndex to prevent IndexOutOfBoundsException
//                if (endIndex > datByteData.length) {
//                    logger.info("Error: Not enough data in datByteData for sample index: " + sampleIndex);
//                    break;
//                }
//
//                //get the bytes for the sample
//                byte[] sampleData = Arrays.copyOfRange(datByteData, startIndex, endIndex);
//                int[] unpackedData;
//                try{
//                    unpackedData = unpackSampleData(sampleData, numOfAnalogChannels, numOfDigitalBytes);
//                }catch (Exception e){
//                    logger.severe("Error: Unable to unpack sample data for sample index: " + sampleIndex+" , "+e.getMessage());
//                    errors.put("analog channel "+i,"Error: Unable to unpack sample data for sample index: " + sampleIndex+" , "+e.getMessage());
//                    continue; // Exit the inner loop or handle accordingly
//                }
//
//                //do operation on the channel data, +2 because first two indices are for the metadata
//                int value = unpackedData[i + 2];
//
//                // Adjust value using a and b coefficients
//                double multiplier = 0;
//                double offSet = 0;
//                if (comtradeConfig.getAnalogChannels().size() > i) {
//                    multiplier = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("multiplier"));
//                    offSet = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("offset"));
//                } else {
//                    logger.info("Index out of bounds for analog channels: " + i);
//                }
//                //1,V0,P0,400,V,0.0027418726,0,0,-32767,32767,1,1,P
//                //multiplier = 0.0027418726, offSet = 0
//                double adjustedValue = value * multiplier + offSet;
//                values.add(adjustedValue);
//            }
//            // Store the values in the corresponding analog channel
//            comtradeConfig.getAnalogChannels().get(i).put("values", values);
//
//        }
//        if(!errors.isEmpty()){
//            return Map.of("error",errors);
//        }
//        return Map.of("success",true);
//    }

//    private Map<String,Object> readBinaryDigital(byte[] datByteData) {
//        // Getting auxiliary variables
//        int numOfAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
//        int numOfDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
//        int numOfDigitalBytes = (int) Math.ceil(numOfDigitalChannels / 16.0);
//        int numberOfSamples = comtradeConfig.getNumOfSamples();
//
//        // Check for valid number of samples
//        if (numberOfSamples <= 0) {
//            System.err.println("Error: Number of samples must be greater than 0.");
//            return Map.of("error","Number of samples must be greater than 0"); // Handle accordingly
//        }
//
//        int bytesPerSample = 4 + 4 + numOfAnalogChannels * 2 + numOfDigitalBytes * 2;
//        HashMap<String, Object> errors = new HashMap<>();
//        // For each digital channel
//        for (int digChanIndex = 0; digChanIndex < numOfDigitalChannels; digChanIndex++) {
//            // Setting initial values for this channel
//            List<Double> values = new ArrayList<>();
//
//            int bitPosition = digChanIndex % 16; // This gives you the position in the byte
//            int mask = (1 << bitPosition); // Create a mask for the corresponding digital channel
//            for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++) {
//                // Unpacking data
//                int startIndex = sampleIndex * bytesPerSample;
//                int endIndex = startIndex + bytesPerSample;
//
//                // Validate startIndex and endIndex to prevent IndexOutOfBoundsException
//                if (endIndex > datByteData.length) {
//                    System.err.println("Error: Not enough data in datByteData for sample index: " + sampleIndex);
//                    break; // Exit the inner loop or handle accordingly
//                }
//
//                byte[] sampleData = Arrays.copyOfRange(datByteData, startIndex, endIndex);
//                int[] unpackedData;
//                try{
//                    unpackedData = unpackSampleData(sampleData, numOfAnalogChannels, numOfDigitalBytes);
//                }catch(Exception e){
//                    System.err.println("Error: Unable to unpack sample data for sample index: " + sampleIndex+" , "+e.getMessage());
//                    errors.put("digital channel "+digChanIndex,"Error: Unable to unpack sample data for sample index: " + sampleIndex+" , "+e.getMessage());
//                    continue;
//                }
//
////                // Fetching the digital channel value
////                if (digChanIndex + numOfAnalogChannels + 2 < unpackedData.length) {
////                    int digitalValue = unpackedData[numOfAnalogChannels + 2]; // Always access the combined digital value
////                    double data = (dchanVal & digitalValue) * (1.0 / dchanVal);
////                    values.add(data);
////                } else {
////                    System.err.println("Warning: Attempted to access index out of bounds for unpackedData");
////                    break;
////                }
//                int digitalValue = unpackedData[numOfAnalogChannels + 2]; // Always access the fixed index 14
//
//                // Calculate the digital channel value for the current index
//                double data = (digitalValue & mask) != 0 ? 1.0 : 0.0; // Check if the bit is set and convert to 1.0 or 0.0
//                values.add(data);
//            }
//
//            // Store the values in the corresponding digital channel
//            comtradeConfig.getDigitalChannels().get(digChanIndex).put("values", values);
//        }
//        if(!errors.isEmpty()){
//            return Map.of("success",false,"error",errors);
//        }
//        return Map.of("success",true);
//    }

}
