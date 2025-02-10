package com.pabu5h.pq_decoder.physical_parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.InflaterInputStream;

@Component
public class PhysicalParser {
    Logger logger = Logger.getLogger(PhysicalParser.class.getName());

    private String filePath;
    private InputStream stream;
    public boolean hasNextRecord;
    private final Set<Long> headerAddresses = new HashSet<>();
    private boolean maximumExceptionsReached = false;
    private CompressionAlgorithm compressionAlgorithm = CompressionAlgorithm.NONE;
    private int compressionStyle = 0;

    public PhysicalParser() {
        // Default constructor
    }

    public PhysicalParser(@Value("${file.path}") String filePath) {
        logger.info("PhysicalParser created with file path: " + filePath);
        this.filePath = filePath;
    }
    public CompletableFuture<Void> openAsync() {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalStateException("Unable to open PQDIF file when no file name has been defined.");
        }

        return CompletableFuture.runAsync(() -> {
            try {
                // Open the stream but do not close it automatically
                this.stream = new FileInputStream(filePath);
                this.hasNextRecord = true;
                System.out.println("File opened successfully: " + filePath);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("File not found: " + filePath, e);
            } catch (IOException e) {
                throw new RuntimeException("Error while opening file: " + filePath, e);
            }
        });
    }


//    public void getNextRecord1() throws IOException {
//        if (stream == null) {
//            logger.info("PQDIF file is not open.");
//            throw new IllegalStateException("PQDIF file is not open.");
//        }
//
//        if (!hasNextRecord) {
//            reset();
//        }
//
//        try {
//            if (stream instanceof FileInputStream) {
//                logger.info("opening pqdif file");
//                FileInputStream fileInputStream = (FileInputStream) stream;
//                if (!fileInputStream.getFD().valid()) {
//                    throw new IllegalStateException("File input stream is closed.");
//                }
//            }
//
//            // Proceed to read the next record's header
//            RecordHeader header = readRecordHeader(stream);
//
//        } catch (IOException e) {
//            throw new IOException("Error reading from stream: " + e.getMessage(), e);
//        }
//
//
//    }

    public Record getNextRecord() throws IOException {
        RecordHeader header;
        if (stream == null) {
            throw new IllegalStateException("PQDIF file is not open.");
        }

        if (!hasNextRecord) {
            reset();
        }

        try {
            if (stream instanceof FileInputStream) {
                FileInputStream fileInputStream = (FileInputStream) stream;
                if (!fileInputStream.getFD().valid()) {
                    throw new IllegalStateException("File input stream is closed.");
                }
            }

            // Proceed to read the next record's header
            header = readRecordHeader(stream);
//            long nextPosition = header.getNextRecordPosition(); // Get next position
//            if (nextPosition > 0) {
//                stream.skip(nextPosition - header.getPosition()); // Adjust streamPosition as you track manually
//                header.setPosition(nextPosition); // Update your stream position tracker
//            } else {
//                hasNextRecord = false; // No more records
//            }
            logger.info(header.toString());
        } catch (IOException e) {
            throw new IOException("Error reading from stream: " + e.getMessage(), e);
        }
        RecordBody body = readRecordBody(header.getBodySize(),stream);

        hasNextRecord = header.getNextRecordPosition() > 0 &&
                header.getNextRecordPosition() < stream.available() &&
                headerAddresses.add(header.getNextRecordPosition()) &&
                !maximumExceptionsReached;

        stream.skip(header.getNextRecordPosition());
        return new Record(header, body);
    }

    public Record getNextRecord(long nextPosition) throws IOException {
        if (stream == null) {
            throw new IllegalStateException("PQDIF file is not open.");
        }

        if (!hasNextRecord) {
            reset(); // Reset if no more records
        }

        RecordHeader header;

        try {
            if (stream instanceof FileInputStream) {
                FileInputStream fileInputStream = (FileInputStream) stream;
                if (!fileInputStream.getFD().valid()) {
                    throw new IllegalStateException("File input stream is closed.");
                }

                // Ensure the stream is buffered for efficient reading
                if (!(stream instanceof BufferedInputStream)) {
                    stream = new BufferedInputStream(stream);
                }
            }

            // Move to the correct position
            if (nextPosition > 0) {
                FileInputStream fileInputStream = (FileInputStream) stream;
                long currentPosition = fileInputStream.getChannel().position();

                if (nextPosition != currentPosition) {
                    fileInputStream.getChannel().position(nextPosition);
                }
            }

            // Read the record header
            header = readRecordHeader(stream); // Reads 64 bytes from the new position

            logger.info("Read header: " + header.toString());

        } catch (IOException e) {
            throw new IOException("Error reading from stream: " + e.getMessage(), e);
        }

        // Read the record body based on header info
        RecordBody body = readRecordBody(header.getBodySize(), stream);

        return new Record(header, body);
    }


   //C# reads in little-endian, while Java's ByteBuffer defaults to big-endian.
   public RecordHeader readRecordHeader(InputStream stream) throws IOException {
       byte[] headerBytes = new byte[64];
       int bytesRead = stream.read(headerBytes);
       if (bytesRead < 64) {
           throw new IOException("Insufficient bytes in header");
       }

       ByteBuffer buffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);

       RecordHeader recordHeader = new RecordHeader();
       recordHeader.setRecordSignature(new UUID(buffer.getLong(), buffer.getLong())); // 16 bytes
       recordHeader.setRecordTypeTag(new UUID(buffer.getLong(), buffer.getLong())); // 16 bytes
       recordHeader.setHeaderSize(buffer.getInt()); // 4 bytes (already in little-endian)
       recordHeader.setBodySize(buffer.getInt()); // 4 bytes
       recordHeader.setNextRecordPosition(buffer.getInt()); // 4 bytes
       recordHeader.setChecksum(Integer.toUnsignedLong(buffer.getInt())); // Convert signed int to unsigned long

       // Correctly reading the reserved 16 bytes
       byte[] reserved = new byte[16];
       buffer.get(reserved); // Read the remaining 16 bytes
       recordHeader.setReserved(reserved);

       return recordHeader;
   }


    private String bytesToSafeString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (b >= 32 && b <= 126) { // Printable ASCII range
                sb.append((char) b);
            } else {
                sb.append('.'); // Replace non-printable characters with a dot
            }
        }
        return sb.toString();
    }


    public RecordBody readRecordBody(int byteSize,InputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalStateException("PQDIF file is not open.");
        }

        if (byteSize == 0) {
            logger.severe("error, byteSize is 0");
            return new RecordBody(null);
        }

        byte[] array = readBytes(byteSize,stream); // Read the bytes from the stream

        // Compute checksum using Adler32 (for validation purpose)
        Adler32 adler = new Adler32();
        adler.update(array);
        long checksum = adler.getValue();

        // Handle compression if specified
        if (compressionAlgorithm == CompressionAlgorithm.ZLIB && compressionStyle != 0) {
            array = decompress(array); // Decompress if needed
        }

        int capacity = array.length;

        // reading data from the byte array from the decompressed or original array
        try (ByteArrayInputStream input = new ByteArrayInputStream(array);

            DataInputStream recordBodyReader = new DataInputStream(input)) {
            int position = capacity - input.available();
            // Use the correct method to read the collection
            CollectionElement collectionElement = readCollection(recordBodyReader);

            // Create the RecordBody using the CollectionElement
            RecordBody recordBody = new RecordBody(collectionElement);  // Correct constructor usage
            recordBody.setChecksum(checksum); // Set the checksum after object creation

            return recordBody;
        }
    }

    public UUID convertIntToUUID(int input) {
        long mostSigBits = (long) input; // The input integer as the most significant bits
        long leastSigBits = 0L; // You can use 0L or another value for the least significant bits

        return new UUID(mostSigBits, leastSigBits);
    }


    public CollectionElement readCollection(DataInputStream recordBodyReader) throws IOException {
        byte[] intBytes = new byte[4]; // For reading an integer (4 bytes)

        // Read the 4 bytes manually
        recordBodyReader.readFully(intBytes);

        // Wrap the bytes in a ByteBuffer and set the byte order to LITTLE_ENDIAN
        ByteBuffer buffer = ByteBuffer.wrap(intBytes).order(ByteOrder.LITTLE_ENDIAN);

        // Now you can read the integer in little-endian order
        int num = buffer.getInt();
        CollectionElement collectionElement = new CollectionElement(num);

        // Keep track of the current position in the stream
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(intBytes);
//        long currentPosition = byteArrayInputStream.pos(); // track the position

        int exceptionCount = 0;
        boolean maximumExceptionsReached = false;

        for (int i = 0; i < num; i++) {
            try {
                collectionElement.addElement(readElement(recordBodyReader)); // Read and add an element
            } catch (Exception e) {
                exceptionCount++;
                if (exceptionCount >= 5) {
                    maximumExceptionsReached = true; // Stop if max exceptions reached
                    break;
                }
            }

            // Check if we've reached the end of the stream or the exception limit
            if (recordBodyReader.available() == 0 || maximumExceptionsReached) {
                break;
            }
        }

        return collectionElement;
    }

    public VectorElement readVector(DataInputStream recordBodyReader, PhysicalType typeOfValue) throws IOException {
        // Create a new VectorElement object
        VectorElement vectorElement = new VectorElement();

        // Read the size (int)
        int size = recordBodyReader.readInt();
        vectorElement.setSize(size); // Set the size directly

        // Set the type of value (e.g., Integer, Float, etc.)
        vectorElement.setTypeOfValue(typeOfValue);

        // Read the values (byte array)
        byte[] values = new byte[size * typeOfValue.getByteSize()];
        recordBodyReader.readFully(values); // Read the specified number of bytes into values

        // Set the values for the vector element (starting from index 0)
//        vectorElement.setValues(values, 0);

        return vectorElement;
    }

    // Method to read a ScalarElement from the DataInputStream
    public ScalarElement readScalar(DataInputStream recordBodyReader, PhysicalType typeOfValue) throws IOException {
        // Create a new ScalarElement object
        ScalarElement scalarElement = new ScalarElement();

        // Set the type of value (e.g., Integer, Float, etc.)
        scalarElement.setTypeOfValue(typeOfValue);

        // Read the scalar value (byte array)
        byte[] value = new byte[typeOfValue.getByteSize()];
        recordBodyReader.readFully(value); // Read the specified number of bytes
//        scalarElement.setValue(value, 0);

        return scalarElement;
    }

    public byte[] decompress(byte[] data) throws IOException {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
             InflaterInputStream inflaterStream = new InflaterInputStream(byteStream)) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inflaterStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            return outputStream.toByteArray();
        }
    }

    // Helper method to read a specific number of bytes from the stream
//    private byte[] readBytes(int length) throws IOException {
//        byte[] buffer = new byte[length];
//        int bytesRead = stream.read(buffer);
//        if (bytesRead < length) {
//            throw new EOFException("Unexpected end of stream while reading header.");
//        }
//        return buffer;
//    }
    private byte[] readBytes(int length,InputStream stream) throws IOException {
        byte[] buffer = new byte[length];
        int totalBytesRead = 0;

        while (totalBytesRead < length) {
            int bytesRead = stream.read(buffer, totalBytesRead, length - totalBytesRead);
            if (bytesRead == -1) {
                throw new EOFException("Unexpected end of stream while reading.");
            }
            totalBytesRead += bytesRead;
        }

        return buffer;
    }

//    public CollectionElement readCollection(DataInputStream recordBodyReader) throws IOException {
//        int num = recordBodyReader.readInt(); // Read the number of elements in the collection
//        CollectionElement collectionElement = new CollectionElement(num); // Create CollectionElement with capacity
//
//        for (int i = 0; i < num; i++) {
//            // Add the element to the collection
//            collectionElement.addElement(readElement(recordBodyReader));
//
//            // Check if the end of the stream or a stopping condition is reached
//            if (recordBodyReader.available() <= 0 || maximumExceptionsReached) {
//                break;
//            }
//        }
//
//        return collectionElement;
//    }

    public Element readElement(DataInputStream recordBodyReader) throws IOException {
        UUID tagOfElement = new UUID(0, 0); // Default empty UUID
        ElementType elementType = ElementType.values()[0]; // Default to first enum value (0)
        PhysicalType typeOfValue = PhysicalType.values()[0]; // Default to first enum value (0)
        long num = recordBodyReader.available() + 28;

        try {
            // Read the tag of the element (16 bytes)
            byte[] tagBytes = new byte[16];
            recordBodyReader.readFully(tagBytes);
            tagOfElement = UUID.nameUUIDFromBytes(tagBytes);

            // Read ElementType and PhysicalType (1 byte each)
            elementType = ElementType.values()[recordBodyReader.readByte()];
            typeOfValue = PhysicalType.values()[recordBodyReader.readByte()];

            // Read some bytes to determine if a link exists
            boolean hasLink = recordBodyReader.readByte() != 0;
            recordBodyReader.readByte(); // Read another byte (unused)

            long offset = recordBodyReader.available() + 8;

            // If num2 is false or elementType is not Scalar, we read a link and check it
            if (!hasLink || elementType != ElementType.SCALAR) {
                int link = recordBodyReader.readInt();
                if (link < 0 || link >= recordBodyReader.available()) {
                    throw new EOFException("Element link is outside the bounds of the file");
                }

                // Seek to the new position in the stream
                recordBodyReader.skipBytes(link);
            }

            // Process the element based on its type
            Element element = null;
            switch (elementType) {
                case COLLECTION:
                    element = readCollection(recordBodyReader);
                    break;
                case SCALAR:
                    element = readScalar(recordBodyReader, typeOfValue);
                    break;
                case VECTOR:
                    element = readVector(recordBodyReader, typeOfValue);
                    break;
                default:
//                    element = new UnknownElement(elementType);
//                    element.setTypeOfValue(typeOfValue);
                    break;
            }

            element.setTagOfElement(tagOfElement);
            recordBodyReader.skipBytes((int) (offset - recordBodyReader.available())); // Adjust to the correct position
            return element;

        } catch (IOException ex) {

            try {
                // Seek to the position if within bounds or to the end if beyond the length
                if (num < recordBodyReader.available()) {
                    recordBodyReader.skipBytes((int) (num - recordBodyReader.available()));
                } else {
                    recordBodyReader.skipBytes(recordBodyReader.available());
                }
            } catch (IOException e) {

            }
            return null;
//            return new ErrorElement(elementType, ex, tagOfElement, typeOfValue);
        }
    }

    // Reset parser state
    private void reset() {
        hasNextRecord = false;
        headerAddresses.clear();
    }

    // Close the file
    public void close() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to close the stream.", e);
            }
        }
    }




}
