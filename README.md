## svc_pq_decoder
PQ Decoder is a specialized software tool for parsing and analyzing Power Quality Data (PQD) and COMTRADE files. It supports both event-based (transients, dips, swells) and trend-based (long-term harmonics, voltage fluctuations) data decoding, enabling detailed power system diagnostics.

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
- invoke the endpoint
  - option 1 - Postman
  - option 2 - Frontend

## Endpoints
- /process_pqd_file
- /process_comtrade_file

## References
- pqdif
  - https://github.com/gemstone/pqdif
- comtrade
  - https://github.com/miguelmoreto/pycomtrade/ 

