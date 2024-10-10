package com.pabu5h.comtrade;
import com.pabu5h.comtrade.config.ComtradeConfig;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComtradeModule {

    private ComtradeConfig comtradeConfig;

    private byte[] binaryData;

    public Map<String,Object> processCfgConfig(InputStream cfgInputStream, InputStream datInputStream) throws IOException {
        comtradeConfig = new ComtradeConfig();

        // Read the configuration file
        try (BufferedReader cfgReader = new BufferedReader(new InputStreamReader(cfgInputStream))) {

            // Reading first three fields (Station, Device, Year), e.g. Phase 5 CT Lab Test,Mon000004053,1999
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

            // Read analog channels
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
            List<int[]> sampleRates = new ArrayList<>();
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

        binaryData = datInputStream.readAllBytes(); // This reads the entire stream into a byte array
        // Process data file based on type
        if ("ASCII".equalsIgnoreCase(comtradeConfig.getFileType())) {
            readAsciiData(datInputStream);
        } else if ("BINARY".equalsIgnoreCase(comtradeConfig.getFileType())) {
            readBinaryData(datInputStream);
        }
        return Map.of("result",comtradeConfig) ;
    }



    public void readBinaryData(InputStream datInputStream) throws IOException {
        // Read all bytes from the InputStream
        if (comtradeConfig.getNumOfAnalogChannels() > 0) {
            readBinaryAnalog();
        }
        if (comtradeConfig.getNumOfDigitalChannels() > 0) {
            readBinaryDigital();
        }
    }


    private void readBinaryAnalog() {
        //Getting auxiliary variables
        int numOfAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
        int numOfDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
        int numOfHeaderBytes = (int) Math.ceil(numOfDigitalChannels / 16.0);
        int numberOfSamples = comtradeConfig.getNumOfSamples();
        // Check for valid number of samples
        if (numberOfSamples <= 0) {
            System.err.println("Error: Number of samples must be greater than 0.");
            return;
        }

        //4 bytes for the first value (32 bits)
        //4 bytes for the second value. (32 bits)
        //2 bytes for each analog channel. (16 bits)
        //2 bytes for each header byte. (16 bits)
        int bytesPerSample = 4 + 4 + numOfAnalogChannels * 2 + numOfHeaderBytes * 2;

        //for each analog channel
        for (int i = 0; i < numOfAnalogChannels; i++) {
            // Check if the analogChannels list has enough elements

            List<Double> values = new ArrayList<>();
            for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++) {
                // nbps = 4+4+nA*2+nH*2
                // data = self.bdata[sampleIndex*nbps:(sampleIndex+1)*nbps]
                int startIndex = sampleIndex * bytesPerSample; //e.g 2 * 108 (start index to read)
                int endIndex = startIndex + bytesPerSample;   // 2*108 + 108 (end index to read)

                // Validate startIndex and endIndex to prevent IndexOutOfBoundsException
                if (endIndex > binaryData.length) {
                    System.err.println("Error: Not enough data in binaryData for sample index: " + sampleIndex);
                    break; // Exit the inner loop or handle accordingly
                }

                //{start index:end index}
                byte[] sampleData = Arrays.copyOfRange(binaryData, startIndex, endIndex);

                //Setting struct string
                int[] unpackedData = unpackSampleData(sampleData, numOfAnalogChannels, numOfHeaderBytes);
                //do operation on the channel data, +2 because first two indices are for the metadata
                int value = unpackedData[i + 2];

                // Adjust value using a and b coefficients
                double multiplier = 0;
                double offSet = 0;
                if (comtradeConfig.getAnalogChannels().size() > i) {
                    multiplier = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("multiplier"));
                    offSet = Double.parseDouble((String) comtradeConfig.getAnalogChannels().get(i).get("offset"));
                } else {
                    System.out.println("Index out of bounds for analog channels: " + i);
                }
                //1,V0,P0,400,V,0.0027418726,0,0,-32767,32767,1,1,P
                //multiplier = 0.0027418726, offSet = 0
                double adjustedValue = value * multiplier + offSet;
                values.add(adjustedValue);
            }
            // Store the values in the corresponding analog channel
            comtradeConfig.getAnalogChannels().get(i).put("values", values);
        }
    }

    private void readBinaryDigital() {
        // Getting auxiliary variables
        int numOfAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
        int numOfDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
        int numOfHeaderBytes = (int) Math.ceil(numOfDigitalChannels / 16.0);
        int numberOfSamples = comtradeConfig.getNumOfSamples();

        // Check for valid number of samples
        if (numberOfSamples <= 0) {
            System.err.println("Error: Number of samples must be greater than 0.");
            return; // Handle accordingly
        }

        int bytesPerSample = 4 + 4 + numOfAnalogChannels * 2 + numOfHeaderBytes * 2;

        // For each digital channel
        for (int digChanIndex = 0; digChanIndex < numOfDigitalChannels; digChanIndex++) {
            // Setting initial values for this channel
            List<Double> values = new ArrayList<>();
            int bitPosition = digChanIndex % 16; // This gives you the position in the byte
            int mask = (1 << bitPosition); // Create a mask for the corresponding digital channel
            for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++) {
                // Unpacking data
                int startIndex = sampleIndex * bytesPerSample;
                int endIndex = startIndex + bytesPerSample;

                // Validate startIndex and endIndex to prevent IndexOutOfBoundsException
                if (endIndex > binaryData.length) {
                    System.err.println("Error: Not enough data in binaryData for sample index: " + sampleIndex);
                    break; // Exit the inner loop or handle accordingly
                }

                byte[] sampleData = Arrays.copyOfRange(binaryData, startIndex, endIndex);
                int[] unpackedData = unpackSampleData(sampleData, numOfAnalogChannels, numOfHeaderBytes);

//                // Fetching the digital channel value
//                if (digChanIndex + numOfAnalogChannels + 2 < unpackedData.length) {
//                    int digitalValue = unpackedData[numOfAnalogChannels + 2]; // Always access the combined digital value
//                    double data = (dchanVal & digitalValue) * (1.0 / dchanVal);
//                    values.add(data);
//                } else {
//                    System.err.println("Warning: Attempted to access index out of bounds for unpackedData");
//                    break;
//                }
                int digitalValue = unpackedData[numOfAnalogChannels + 2]; // Always access the fixed index 14

                // Calculate the digital channel value for the current index
                double data = (digitalValue & mask) != 0 ? 1.0 : 0.0; // Check if the bit is set and convert to 1.0 or 0.0
                values.add(data);
            }

            // Store the values in the corresponding digital channel
            comtradeConfig.getDigitalChannels().get(digChanIndex).put("values", values);
        }
    }


    private int[] unpackSampleData(byte[] sampleData, int numOfAnalogShorts, int numOfHeaderShorts) {
        ByteBuffer buffer = ByteBuffer.wrap(sampleData);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Assuming little-endian based on the Python example

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



    public void readAsciiData(InputStream datInputStream) throws IOException {
        String content = new String(datInputStream.readAllBytes(), StandardCharsets.UTF_8);
        List<String> lines = Arrays.asList(content.split("\n"));
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
                System.out.println("Index out of bounds for analog channels: " + i);
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
    }


    public Map<String, Object> convertConfigFieldsToMap(ComtradeConfig comtradeConfig) {
        LinkedHashMap<String, Object> configMap = new LinkedHashMap<>();
        configMap.put("stationName", comtradeConfig.getStationName());
        configMap.put("deviceId", comtradeConfig.getDeviceId());
        configMap.put("revYear", String.valueOf(comtradeConfig.getRevYear()));
        configMap.put("numOfChannels", String.valueOf(comtradeConfig.getNumOfChannels()));
        configMap.put("numOfAnalogChannels", String.valueOf(comtradeConfig.getNumOfAnalogChannels()));
        configMap.put("numOfDigitalChannels", String.valueOf(comtradeConfig.getNumOfDigitalChannels()));
        configMap.put("lineFrequency", String.valueOf(comtradeConfig.getLineFrequency()));
        configMap.put("startTimestamp", comtradeConfig.getStartTimestamp());
        configMap.put("endTimestamp", comtradeConfig.getEndTimestamp());
        configMap.put("fileType", comtradeConfig.getFileType());
        configMap.put("timeMultiplier", String.valueOf(comtradeConfig.getTimeMultiplier()));
        configMap.put("numOfSamples", String.valueOf(comtradeConfig.getNumOfSamples()));
        configMap.put("sampleRates",  String.valueOf(comtradeConfig.getSampleRates()));
        configMap.put("analogChannels", comtradeConfig.getAnalogChannels());
        configMap.put("digitalChannels", comtradeConfig.getDigitalChannels());
        return Map.of("result",configMap);
    }

}
