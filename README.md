## svc_pq_decoder
PQ Decoder Service (containing decoders PQDIF and COMTRADE file formats) is a tool developed to decode COMTRADE (Common Format for Transient Data Exchange) files. It reads in .cfg (configuration) and .dat (data) files, enabling users to extract, process, and analyze Power Quality (PQ) data.
The primary goal of this project is to convert COMTRADE files into more user-friendly formats, such as CSV or JSON, making it easier for users to inspect and analyze the data using familiar tools.

## Features
- Parse COMTRADE files (.cfg and .dat) according to the IEEE C37.111 standard.
- Extract both analog and digital signal data.
- Handle various formats including binary and ASCII
- Generate output in formats like CSV and JSON

## installation guide
- download intellij
  - Download and install IntelliJ IDEA for your development environment.
- pull the latest pq_decoder
  - git clone https://github.com/pabu500/pq_decoder.git repo
- run the backend
  - open the intellij and run the backend to start service
- run the frontend
  - have some UI to upload the cfg and dat file byte data to the pq_decoder backend
- API Endpoint
  - Endpoint: /process_pqd
  - Ensure the UI is configured to send the files to this endpoint.
 
### Required params
- cfg multipart
- dat multipart
- operation
  - plotGraph
  - downloadCsvZip
  - downloadJsonZip


