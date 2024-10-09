package com.pabu5h.comtrade;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComtradeReader {

    private ComtradeConfig comtradeConfig;
    private List<Map<String, Object>> analogChannels;
    private List<Map<String, Object>> digitalChannels;
    private byte[] binaryData;

    public ComtradeReader() {
        this.analogChannels = new ArrayList<>();
        this.digitalChannels = new ArrayList<>();
    }

    public void reset() {
        analogChannels.clear();
        digitalChannels.clear();
    }

    public void readAsciiData(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));

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
                    .map(row -> row.get(index)) // Use 'index' instead of 'i'
                    .collect(Collectors.toList());
            List<Double> adjustedValues = adjustAnalogValues(values, index); // Use 'index' here as well
            comtradeConfig.getAnalogChannels().get(index).put("values", adjustedValues.toString()); // Use 'index' here
        }

        data = data.stream().map(row -> row.subList(nAnalogChannels, row.size())).collect(Collectors.toList());

        int nDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
        for (int i = 0; i < nDigitalChannels; i++) { // Corrected here
            final int index = i;
            List<Double> values = data.stream()
                    .map(row -> row.get(index))
                    .collect(Collectors.toList());
            List<Double> adjustedValues = adjustAnalogValues(values, i);
            digitalChannels.get(i).put("values", adjustedValues);
        }

        System.out.println("ASCII Analog Data: " + analogChannels);
        System.out.println("ASCII Digital Data: " + digitalChannels);
    }

    private List<Double> adjustAnalogValues(List<Double> values, int digChanIndex) {
        // Adjust analog values by multiplying with 'a' and adding 'b' coefficients
        double a = Double.parseDouble(comtradeConfig.getAnalogChannels().get(digChanIndex).get("scale_factor"));
        double b = Double.parseDouble(comtradeConfig.getAnalogChannels().get(digChanIndex).get("offset"));

        return values.stream().map(value -> value * a + b).collect(Collectors.toList());
    }

    public void readBinaryData(String filePath) throws IOException {
        // open .dat file and read binary data
        binaryData = Files.readAllBytes(Paths.get(filePath));

        readBinaryAnalog();
        readBinaryDigital();
        System.out.println("comtradeConfig: " + comtradeConfig);
    }

    private void readBinaryAnalog() {
        //Getting auxiliary variables
        int numOfAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
        int numOfDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
        int numOfHeaderBytes = (int) Math.ceil(numOfDigitalChannels / 16.0);
        List<int[]> sampleRates = comtradeConfig.getSampleRates();
        //get last sample rate
        int numOfSamples = sampleRates.getLast()[1];

        // Check for valid number of samples
        if (numOfSamples <= 0) {
            System.err.println("Error: Number of samples must be greater than 0.");
            return; // Handle accordingly
        }

        // nA + nH <- str_struct = "ii{0}h".format(nA + nH)
        int numOfAnalogShorts = numOfAnalogChannels + numOfHeaderBytes;
        int bytesPerSample = 4 + 4 + numOfAnalogChannels * 2 + numOfHeaderBytes * 2;

        //for each analog channel
        for (int i = 0; i < numOfAnalogChannels; i++) {
            // Check if the analogChannels list has enough elements

            List<Double> values = new ArrayList<>();
            for (int sampleIndex = 0; sampleIndex < numOfSamples; sampleIndex++) {
                //data = self.bdata[sidx*nbps:(sidx+1)*nbps]
                int startIndex = sampleIndex * bytesPerSample;
                int endIndex = startIndex + bytesPerSample;

                // Validate startIndex and endIndex to prevent IndexOutOfBoundsException
                if (endIndex > binaryData.length) {
                    System.err.println("Error: Not enough data in binaryData for sample index: " + sampleIndex);
                    break; // Exit the inner loop or handle accordingly
                }

                byte[] sampleData = Arrays.copyOfRange(binaryData, startIndex, endIndex);

                //Setting struct string
                int[] unpackedData = unpackSampleData(sampleData, numOfAnalogShorts, numOfHeaderBytes);

                int value = unpackedData[i + 2];

//                int value = extractAnalogValue(sampleData, i);

                // Adjust value using a and b coefficients
                double multiplier = 0;
                double offSet = 0;
                if (comtradeConfig.getAnalogChannels().size() > i) {
                    multiplier = Double.parseDouble(comtradeConfig.getAnalogChannels().get(i).get("multiplier"));
                    offSet = Double.parseDouble(comtradeConfig.getAnalogChannels().get(i).get("offset"));
                } else {
                    System.out.println("Index out of bounds for analog channels: " + i);
                }

                double adjustedValue = value * multiplier + offSet;
                values.add(adjustedValue);
            }
            System.out.println(values);
            // Store the values in the corresponding analog channel
            comtradeConfig.getAnalogChannels().get(i).put("values", values.toString());
        }
    }

    private void readBinaryDigital() {
        // Getting auxiliary variables
        int numOfAnalogChannels = comtradeConfig.getNumOfAnalogChannels();
        int numOfDigitalChannels = comtradeConfig.getNumOfDigitalChannels();
        int numOfHeaderBytes = (int) Math.ceil(numOfDigitalChannels / 16.0);
        List<int[]> sampleRates = comtradeConfig.getSampleRates();

        // Get last sample rate
        int numOfSamples = sampleRates.getLast()[1];

        // Check for valid number of samples
        if (numOfSamples <= 0) {
            System.err.println("Error: Number of samples must be greater than 0.");
            return; // Handle accordingly
        }

        // nA + nH for struct string = "ii{0}h{1}H".format(numOfAnalogChannels, numOfHeaderBytes)
        int numOfShorts = numOfAnalogChannels + numOfHeaderBytes;
        int bytesPerSample = 4 + 4 + numOfAnalogChannels * 2 + numOfHeaderBytes * 2;

        // For each digital channel
        for (int digChanIndex = 0; digChanIndex < numOfDigitalChannels; digChanIndex++) {
            // Byte number
            int byteNum = (int) Math.ceil(digChanIndex / 16.0);

            // Digital channel value
            int dchanVal = (1 << (digChanIndex - (byteNum - 1) * 16));

            // Setting initial values for this channel
            List<Double> values = new ArrayList<>();

            for (int sidx = 0; sidx < numOfSamples; sidx++) {
                // Unpacking data
                int startIndex = sidx * bytesPerSample;
                int endIndex = startIndex + bytesPerSample;

                // Validate startIndex and endIndex to prevent IndexOutOfBoundsException
                if (endIndex > binaryData.length) {
                    System.err.println("Error: Not enough data in binaryData for sample index: " + sidx);
                    break; // Exit the inner loop or handle accordingly
                }

                byte[] sampleData = Arrays.copyOfRange(binaryData, startIndex, endIndex);
                int[] unpackedData = unpackSampleData(sampleData, numOfShorts, numOfHeaderBytes);

                // Fetching the digital channel value
                int digitalValue = unpackedData[digChanIndex + 2 + numOfAnalogChannels]; // Adjusted index for digital channel
                double data = (dchanVal & digitalValue) * (1.0 / dchanVal); // Calculate the channel value
                values.add(data);
            }

            // Store the values in the corresponding digital channel
            comtradeConfig.getDigitalChannels().get(digChanIndex).put("values", values.toString());
        }
    }


    private int[] unpackSampleData(byte[] sampleData, int numOfShorts, int numOfHeaderShorts) {
        ByteBuffer buffer = ByteBuffer.wrap(sampleData);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Assuming little-endian based on the Python example

        // Read the two integers (4 bytes each)
        int int1 = buffer.getInt();
        int int2 = buffer.getInt();

        // Read the short values for analog channels (2 bytes each)
        int[] analogShortValues = new int[numOfShorts];
        for (int i = 0; i < numOfShorts; i++) {
            analogShortValues[i] = buffer.getShort();
        }

        // Read the short values for header bytes (2 bytes each)
        int[] headerShortValues = new int[numOfHeaderShorts];
        for (int i = 0; i < numOfHeaderShorts; i++) {
            headerShortValues[i] = buffer.getShort(); // If unsigned, handle conversion appropriately
        }

        // Combine all values into one array
        int[] result = new int[2 + numOfShorts  + numOfHeaderShorts];
        result[0] = int1;
        result[1] = int2;
        System.arraycopy(analogShortValues, 0, result, 2, numOfShorts);
        System.arraycopy(headerShortValues, 0, result, 2 + numOfShorts, numOfHeaderShorts);

        return result;
    }

    public void readComtradeConfig(String cfgPath, String datPath) throws IOException {
        comtradeConfig = new ComtradeConfig();

        // Read the configuration file
        try (BufferedReader cfgReader = new BufferedReader(new FileReader(cfgPath))) {

            // Reading first three fields (Station, Device, Year)
            String[] header = cfgReader.readLine().split(",");
            comtradeConfig.setStationName(header[0]);
            comtradeConfig.setDeviceId(header[1]);
            comtradeConfig.setRevYear(Integer.parseInt(header[2]));

            // Read number of analog and digital channels
            String[] channelInfo = cfgReader.readLine().split(",");
            comtradeConfig.setNumOfChannels(Integer.parseInt(channelInfo[0]));
            comtradeConfig.setNumOfAnalogChannels(Integer.parseInt(channelInfo[1].replaceAll("A", "")));
            comtradeConfig.setNumOfDigitalChannels(Integer.parseInt(channelInfo[2].replaceAll("D", "")));

            // Read analog channels
            List<Map<String, String>> analogChannels = new ArrayList<>();
            for (int i = 0; i < comtradeConfig.getNumOfAnalogChannels(); i++) {
                String[] analogLine = cfgReader.readLine().split(",");
                Map<String, String> Channel = new HashMap<>();
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
            List<Map<String, String>> digitalChannels = new ArrayList<>();
            for (int i = 0; i < comtradeConfig.getNumOfDigitalChannels(); i++) {
                String[] analogLine = cfgReader.readLine().split(",");
                Map<String, String> Channel = new HashMap<>();
                Channel.put("channel_num", analogLine[0]);
                Channel.put("name", analogLine[1]);
                Channel.put("phase_id", analogLine[2]);
                Channel.put("component_id", analogLine[3]);
                Channel.put("unit", analogLine[4]);
                Channel.put("state", analogLine[5]);
                digitalChannels.add(Channel);
            }
            comtradeConfig.setDigitalChannels(digitalChannels);

            // Read line frequency
            comtradeConfig.setLineFrequency(Integer.parseInt(cfgReader.readLine()));

            // Read sample rate
            List<int[]> sampleRates = new ArrayList<>();
            int numSampleRates = Integer.parseInt(cfgReader.readLine());
            for (int i = 0; i < numSampleRates; i++) {
                String[] sampleRateInfo = cfgReader.readLine().split(",");
                sampleRates.add(new int[]{
                        Integer.parseInt(sampleRateInfo[0]),
                        Integer.parseInt(sampleRateInfo[1])
                });
            }
            comtradeConfig.setSampleRates(sampleRates);

            // Read start and end timestamps
            comtradeConfig.setStartTimestamp(cfgReader.readLine());
            comtradeConfig.setEndTimestamp(cfgReader.readLine());

            // Read file type (ASCII or BINARY)
            comtradeConfig.setFileType(cfgReader.readLine());

            // Read time multiplier
            comtradeConfig.setTimeMultiplier(Integer.parseInt(cfgReader.readLine()));
        }

        // Process data file based on type
        if ("ASCII".equalsIgnoreCase(comtradeConfig.getFileType())) {
            readAsciiData(datPath);
        } else if ("BINARY".equalsIgnoreCase(comtradeConfig.getFileType())) {
            readBinaryData(datPath);
        }

    }

    private int extractDigitalValue(byte[] sampleData, int numOfAnalogChannels, int byteNum, int dchanVal) {
        ByteBuffer buffer = ByteBuffer.wrap(sampleData);
        short digitalWord = buffer.getShort(8 + numOfAnalogChannels * 2 + byteNum * 2);
        return (digitalWord & dchanVal) > 0 ? 1 : 0;
    }
}
