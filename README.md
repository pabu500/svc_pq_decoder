## svc_pq_decoder
PQ Decoder is a tool developed to decode PQD(Power Quality Data) and COMTRADE (Common Format for Transient Data Exchange) files for both event-based and trend-based data. 

## Features
- Parse PQD files (.pqd)
- Parse COMTRADE files (.cfg and .dat)
- Extract waveform Data (Events)
- Extract both analog and digital signal data.
- Handle various formats including binary and ASCII
- CSV and JSON download

### Params
- multipart files  (required)
  - .cfg file (for comtrade)
  - .dat file (for comtrade)
  - .pqd file (for pqd)
- operation (required)
  - plotGraph
  - downloadCsvZip
  - downloadJsonZip
- sample_step (optional)
- filename(optional)

## Installation guide
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

## Endpoints
- /process_pqd_file
- /process_comtrade_file

## References
- pqdif
  - https://github.com/gemstone/pqdif

