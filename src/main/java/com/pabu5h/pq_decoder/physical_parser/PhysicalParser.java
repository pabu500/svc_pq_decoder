package com.pabu5h.pq_decoder.physical_parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.InflaterInputStream;

import org.springframework.beans.factory.annotation.Value;

import com.pabu5h.pq_decoder.util.GUID;
import org.springframework.stereotype.Component;

@Component
public class PhysicalParser {
    Logger logger = Logger.getLogger(PhysicalParser.class.getName());

    private String filePath;
    private InputStream stream;
    public boolean hasNextRecord;
    public long currentStreamPosition = 0;
    private final Set<Long> headerAddresses = new HashSet<>();
    private boolean maximumExceptionsReached = false;
    public CompressionAlgorithm compressionAlgorithm = CompressionAlgorithm.None;
    public CompressionStyle compressionStyle = CompressionStyle.None;

    public PhysicalParser(@Value("${file.path}") String filePath) {
        logger.info("PhysicalParser created with file path: " + filePath);
        this.filePath = filePath;
    }
    
    public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
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

	public Record getNextRecord(/* long nextPosition */) throws IOException {
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
//                if (!(stream instanceof BufferedInputStream)) {
//                    stream = new BufferedInputStream(stream);
//                }
            }

            // Move to the correct position
//            if (nextPosition > 0) {
//                FileInputStream fileInputStream = (FileInputStream) stream;
//                long currentPosition = fileInputStream.getChannel().position();
//
//                if (nextPosition != currentPosition) {
//                    fileInputStream.getChannel().position(nextPosition);
//                }
//            }

            // Read the record header
            header = readRecordHeader(); // Reads 64 bytes from the new position

            logger.info("Read header: " + header.toString());

        } catch (IOException e) {
            throw new IOException("Error reading from stream: " + e.getMessage(), e);
        }

        // Read the record body based on header info
        RecordBody body = readRecordBody(header.getBodySize());

        if (body.getCollection().getTagOfElement() == GUID.Empty) {
        	body.getCollection().setTagOfElement(header.getRecordTypeTag());
        }
        
        long streamMaxSize = ((FileInputStream) stream).available() + ((FileInputStream) stream).getChannel().position();
        headerAddresses.add(header.getNextRecordPosition());
        hasNextRecord = header.getNextRecordPosition() > 0
				&& header.getNextRecordPosition() < streamMaxSize /* m_stream.Length *//* && !MaximumExceptionsReached */;
        ((FileInputStream) stream).getChannel().position(header.getNextRecordPosition());
        currentStreamPosition = header.getNextRecordPosition();
        
        return new Record(header, body);
    }


   //C# reads in little-endian, while Java's ByteBuffer defaults to big-endian.
   public RecordHeader readRecordHeader() throws IOException {
	   
	   
       RecordHeader recordHeader = new RecordHeader();
       recordHeader.setPosition(((FileInputStream) stream).getChannel().position());
	   
       byte[] headerBytes = new byte[64];
//       int bytesRead = stream.read(headerBytes);
       
//       stream = new FileInputStream(new File("C:\\Users\\wp3\\Downloads\\svc_pq_decoder\\src\\main\\resources\\test.txt"));
       int size = 64;
       int num;
       for (int offset = 0; offset < size; offset += num) {
           int count = size - offset;
           num = stream.read(headerBytes, offset, count);
           if (num == 0) {
               throw new IOException("Unexpected end of stream encountered");
           }
       }
//       if (bytesRead < 64) {
//           throw new IOException("Insufficient bytes in header");
//       }

       ByteBuffer buffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);


       byte[] sig = new byte[16];
       buffer.get(sig);
       recordHeader.setRecordSignature(new GUID(sig)); // 16 bytes
       
       
       // type tag
       byte[] tag = new byte[16];
       buffer.get(tag);
       recordHeader.setRecordTypeTag(new GUID(tag)); // 16 bytes
       // end type tag
       
       
       recordHeader.setHeaderSize(buffer.getInt()); // 4 bytes (already in little-endian)
       recordHeader.setBodySize(buffer.getInt()); // 4 bytes
       recordHeader.setNextRecordPosition(buffer.getInt()); // 4 bytes
       recordHeader.setChecksum(Integer.toUnsignedLong(buffer.getInt())); // Convert signed int to unsigned long
       recordHeader.setRecordType();
       // Correctly reading the reserved 16 bytes
       byte[] reserved = new byte[16];
       buffer.get(reserved); // Read the remaining 16 bytes
       recordHeader.setReserved(reserved);
//4a111440-e49f-11cf-9900-505144494600 - 89738606-f1c3-11cf-9d89-0080c72e70a3
       return recordHeader;
   }


//    public RecordBody readRecordBody(int byteSize) throws IOException {
//        if (stream == null) {
//            throw new IllegalStateException("PQDIF file is not open.");
//        }
//
//        if (byteSize == 0) {
//            logger.severe("error, byteSize is 0");
//            return new RecordBody(null);
//        }
//
//        byte[] array = readBytes(byteSize,stream); // Read the bytes from the stream
//
//        // Compute checksum using Adler32 (for validation purpose)
//        Adler32 adler = new Adler32();
//        adler.update(array);
//        long checksum = adler.getValue();
//
//        // Handle compression if specified
//        if (compressionAlgorithm == CompressionAlgorithm.ZLIB && compressionStyle != 0) {
//            array = decompress(array); // Decompress if needed
//        }
//
//        int capacity = array.length;
//
//        // reading data from the byte array from the decompressed or original array
//        try (ByteArrayInputStream input = new ByteArrayInputStream(array);
//
//            DataInputStream recordBodyReader = new DataInputStream(input)) {
//            int position = capacity - input.available();
//            // Use the correct method to read the collection
//            CollectionElement collectionElement = readCollection(recordBodyReader);
//
//            // Create the RecordBody using the CollectionElement
//            RecordBody recordBody = new RecordBody(collectionElement);  // Correct constructor usage
//            recordBody.setChecksum(checksum); // Set the checksum after object creation
//
//            return recordBody;
//        }
//    }

    public RecordBody readRecordBody(int byteSize) throws IOException {
        if (stream == null) {
            throw new IllegalStateException("PQDIF file is not open.");
        }

        if (byteSize == 0) {
            return new RecordBody(null); // Or any default value, since GUID.Empty in C# is a zero UUID
        }

        //reads the bytes into the array and returns the byte data.
        //somehow the value obtained from the stream is different  from original code, however checksum value is same
        byte[] array = readBytesAsync(byteSize); // Read the bytes from the stream
        long checksum = Adler32(array); // Compute checksum (use a utility method to compute Adler32)

        if (compressionAlgorithm == CompressionAlgorithm.Zlib && compressionStyle != CompressionStyle.None) {
            array = decompress(array); // Decompress if needed
        }

        // Wrap the byte array into a ByteBuffer, with LITTLE_ENDIAN byte order
        ByteBuffer buffer = ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN);

        // using a ByteBuffer in Java with ByteOrder.LITTLE_ENDIAN
        // is equivalent to using MemoryStream and BinaryReader in C#
        return new RecordBody(readCollection(buffer));
    }

    //a checksum algorithm designed to quickly detect errors in data.
    // It generates a 32-bit checksum value,
    // which is used to verify the integrity of data during transmission or storage
    public long Adler32(byte[] array) {
        Adler32 adler = new Adler32();
        adler.update(array);
        return adler.getValue();  // Get the checksum value
    }


    public CollectionElement readCollection(ByteBuffer buffer) {
        // Read the first integer (num) which tells us how many elements there are
        int num = buffer.getInt();
        // create and populate a CollectionElement object with a number of elements
        // based on the integer num read from the buffer.
        CollectionElement collectionElement = new CollectionElement(num);

        // Loop through and read each element, up to 'num' elements
        for (int i = 0; i < num; i++) {
            if (buffer.remaining() == 0 || maximumExceptionsReached) {
                break;  // Stop if there are no more bytes or max exceptions reached
            }

            collectionElement.addElement(readElement(buffer));  // Read each element
        }

        return collectionElement;
    }

//    public CollectionElement readCollection(DataInputStream recordBodyReader) throws IOException {
//        byte[] intBytes = new byte[4]; // For reading an integer (4 bytes)
//
//        // Read the 4 bytes manually
//        recordBodyReader.readFully(intBytes);
//
//        // Wrap the bytes in a ByteBuffer and set the byte order to LITTLE_ENDIAN
//        ByteBuffer buffer = ByteBuffer.wrap(intBytes).order(ByteOrder.LITTLE_ENDIAN);
//
//        // Now you can read the integer in little-endian order
//        int num = buffer.getInt();
//        CollectionElement collectionElement = new CollectionElement(num);
//
//        // Keep track of the current position in the stream
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(intBytes);
////        long currentPosition = byteArrayInputStream.pos(); // track the position
//
//        int exceptionCount = 0;
//        boolean maximumExceptionsReached = false;
//
//        for (int i = 0; i < num; i++) {
//            try {
//                collectionElement.addElement(readElement(recordBodyReader)); // Read and add an element
//            } catch (Exception e) {
//                exceptionCount++;
//                if (exceptionCount >= 5) {
//                    maximumExceptionsReached = true; // Stop if max exceptions reached
//                    break;
//                }
//            }
//
//            // Check if we've reached the end of the stream or the exception limit
//            if (recordBodyReader.available() == 0 || maximumExceptionsReached) {
//                break;
//            }
//        }
//
//        return collectionElement;
//    }

    public VectorElement readVector(ByteBuffer recordBodyReader, PhysicalType typeOfValue) throws IOException {
        // Create a new VectorElement object
        VectorElement vectorElement = new VectorElement();

        // Read the size (int)
        int size = recordBodyReader.getInt();
        vectorElement.setSize(size); // Set the size directly

        // Set the type of value (e.g., Integer, Float, etc.)
        vectorElement.setTypeOfValue(typeOfValue);

        // Read the values (byte array)
        byte[] values = new byte[size * typeOfValue.getByteSize()];
        recordBodyReader.get(values); // Read the specified number of bytes into values

        // Set the values for the vector element (starting from index 0)
        vectorElement.setValues(values, 0);

        return vectorElement;
    }

    // Method to read a ScalarElement from the DataInputStream
    public ScalarElement readScalar(ByteBuffer recordBodyReader, PhysicalType typeOfValue) throws IOException {
        // Create a new ScalarElement object
        ScalarElement scalarElement = new ScalarElement();

        // Set the type of value (e.g., Integer, Float, etc.)
        scalarElement.setTypeOfValue(typeOfValue);

        
        // Read the scalar value (byte array)
        byte[] value = new byte[typeOfValue.getByteSize()];
        recordBodyReader.get(value); // Read the specified number of bytes
        scalarElement.setValue(value, 0);

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
    private byte[] readBytesAsync(int size) throws IOException {
        if (stream == null) {
            throw new IllegalStateException("Stream is not open.");
        }

        byte[] data = new byte[size];
        int num;
        int offset = 0;((FileInputStream) stream).getChannel().position();

        while (offset < size) {
            int count = size - offset;
            num = stream.read(data, offset, count);
            if (num == -1) {
                throw new EOFException("Unexpected end of stream encountered.");
            }
            offset += num;
        }

        return data;
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

//    public Element readElement(DataInputStream recordBodyReader) throws IOException {
//        UUID tagOfElement = new UUID(0, 0); // Default empty UUID
//        ElementType elementType = ElementType.values()[0]; // Default to first enum value (0)
//        PhysicalType typeOfValue = PhysicalType.values()[0]; // Default to first enum value (0)
//        long num = recordBodyReader.available() + 28;
//
//        try {
//            // Read the tag of the element (16 bytes)
//            byte[] tagBytes = new byte[16];
//            recordBodyReader.readFully(tagBytes);
//            tagOfElement = UUID.nameUUIDFromBytes(tagBytes);
//
//            // Read ElementType and PhysicalType (1 byte each)
//            elementType = ElementType.values()[recordBodyReader.readByte()];
//            typeOfValue = PhysicalType.values()[recordBodyReader.readByte()];
//
//            // Read some bytes to determine if a link exists
//            boolean hasLink = recordBodyReader.readByte() != 0;
//            recordBodyReader.readByte(); // Read another byte (unused)
//
//            long offset = recordBodyReader.available() + 8;
//
//            // If num2 is false or elementType is not Scalar, we read a link and check it
//            if (!hasLink || elementType != ElementType.SCALAR) {
//                int link = recordBodyReader.readInt();
//                if (link < 0 || link >= recordBodyReader.available()) {
//                    throw new EOFException("Element link is outside the bounds of the file");
//                }
//
//                // Seek to the new position in the stream
//                recordBodyReader.skipBytes(link);
//            }
//
//            // Process the element based on its type
//            Element element = null;
//            switch (elementType) {
//                case COLLECTION:
//                    element = readCollection(recordBodyReader);
//                    break;
//                case SCALAR:
//                    element = readScalar(recordBodyReader, typeOfValue);
//                    break;
//                case VECTOR:
//                    element = readVector(recordBodyReader, typeOfValue);
//                    break;
//                default:
////                    element = new UnknownElement(elementType);
////                    element.setTypeOfValue(typeOfValue);
//                    break;
//            }
//
//            element.setTagOfElement(tagOfElement);
//            recordBodyReader.skipBytes((int) (offset - recordBodyReader.available())); // Adjust to the correct position
//            return element;
//
//        } catch (IOException ex) {
//
//            try {
//                // Seek to the position if within bounds or to the end if beyond the length
//                if (num < recordBodyReader.available()) {
//                    recordBodyReader.skipBytes((int) (num - recordBodyReader.available()));
//                } else {
//                    recordBodyReader.skipBytes(recordBodyReader.available());
//                }
//            } catch (IOException e) {
//
//            }
//            return null;
////            return new ErrorElement(elementType, ex, tagOfElement, typeOfValue);
//        }
//    }

	public Element readElement(ByteBuffer buffer) {
	    GUID tagOfElement = new GUID(0, 0);  // Initialize with an empty UUID
	    ElementType elementType = ElementType.values()[0]; // Assuming unknown as default
	    PhysicalType typeOfValue = PhysicalType.values()[0]; // Default PhysicalType
	    long position = buffer.position() + 28; // Track position, similar to C# code
	
	    try {
	        buffer.order(ByteOrder.LITTLE_ENDIAN);
	        // Read the GUID (16 bytes)
	        
	
	        byte[] guidBytes = new byte[16];
	        buffer.get(guidBytes);
	        tagOfElement = new GUID(guidBytes);
	//        tagOfElement = new UUID(ByteBuffer.wrap(guidBytes).getLong(), ByteBuffer.wrap(guidBytes, 8, 8).getLong());
	
	        // Read ElementType (1 byte) and PhysicalType (1 byte)
	//        elementType = ElementType.fromValue(Byte.toUnsignedInt(buffer.get()));
	        byte eleNext = buffer.get();
	        elementType = ElementType.fromValue(eleNext);
	        
	        byte typeNext = buffer.get();
	        typeOfValue = PhysicalType.fromValue(typeNext);
	
	        // Read the boolean flag (1 byte)
	        boolean num2 = buffer.get() != 0;
	        buffer.get(); // Skip 1 byte (like in C# code)
	
	        long offset = buffer.position() + 8;  // Track the offset (like C# code)
	
	        if (!num2 || elementType != ElementType.SCALAR) {
	            int num3 = buffer.getInt();  // Read an int (4 bytes)
	            if (num3 < 0 || num3 >= buffer.capacity()) {
	                throw new IllegalArgumentException("Element link is outside the bounds of the buffer");
	            }
	
	            buffer.position(num3);  // Seek to the new position in the buffer
	        }
	
	        Element element = null;
	        switch (elementType) {
	            case COLLECTION:
	                element = readCollection(buffer);  // Call readCollection for COLLECTION type
	                break;
	            case SCALAR:
	                element = readScalar(buffer, typeOfValue);  // Call readScalar for SCALAR type
	                break;
	            case VECTOR:
	                element = readVector(buffer, typeOfValue);  // Call readVector for VECTOR type
	                break;
	            default:
//	                element = new UnknownElement(elementType);
//	                element.setTypeOfValue(typeOfValue);
	                break;
	        }
	
	        element.setTagOfElement(tagOfElement);
	        buffer.position((int) offset);  // Restore the buffer position to the correct offset
	        return element;
	
	    } catch (Exception ex) {
	        // Add exception to your list (or log it)
	    	ex.printStackTrace();
	        if (position < buffer.limit()) {
	            buffer.position((int) position);  // Reset position to a safe point
	        } else {
	            buffer.position(buffer.limit());  // Seek to the end of the buffer if out of bounds
	        }
	        Element element = null;
	        // Return an error element in case of exception
	        return element;
	    }
	}

	private byte getByte(ByteBuffer byteBuffer) {
		byte[] bytes = new byte[1];
		byteBuffer.get(bytes);
		return bytes[0];
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

//	public static void print(InputStream stream, String mark) {
//		try {
//			byte[] headerBytes = new byte[64];
//
//			int size = 64;
//			int num;
//			for (int offset = 0; offset < size; offset += num) {
//				int count = size - offset;
//				num = stream.read(headerBytes, offset, count);
//				if (num == 0) {
//					throw new IOException("Unexpected end of stream encountered");
//				}
//			}
//
//			ByteBuffer byteBuffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
//			System.out.println("==================");
//			System.out.println(byteBuffer.getLong());
//			System.out.println(byteBuffer.getLong());
//			System.out.println(byteBuffer.getLong());
//			System.out.println(byteBuffer.getLong());
//			
//			System.out.println("==================");
//			
//			System.out.println(byteBuffer.getInt());
//			System.out.println(byteBuffer.getInt());
//			System.out.println(byteBuffer.getInt());
//			System.out.println(byteBuffer.getInt());
//			
//			ByteBuffer.wrap(new byte[] {(byte) 8, (byte)3, (byte)0, (byte)0}).order(ByteOrder.LITTLE_ENDIAN).getInt();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


//	public static void main(String[] args) throws FileNotFoundException {
//		print(new java.io.FileInputStream("C:\\Users\\wp3\\Downloads\\svc_pq_decoder\\src\\main\\resources\\TAMPINES NT 22kV DE_20230208001841.pqd"), "lol");
//	}

    
    public static void main(String[] args) {

	}
}
