## Project Title: Advanced Number Availability & Prime Extraction Service

### Overview

this project builds a Spring Boot CLI tool called ***analysis*** that performs availability analysis and prime extraction across a configurable
number of randomly generated arrays. 

### Language and Tools
    Java 25
    Spring Boot 4.0.5
    JUnit & Mokito Testing
    JaCoCo code coverage report
    Maven
    Docker

### Configuration
  There is a default configuration file (***application.yml***) built into the project. Users can specify their own configuration file (by the same filename: ***application.yml***) if needed. 

### Build and Run
1. Clone this project from GitHub to local machine
2. In the project root directory run build command below. This will run Unit tests and build the jar, and then create a docker image:
> mvn clean package docker:build
3. To execute the CLI tool, copy batch script ***run.cmd*** from the project rool and run:
> run.cmd [config], where config is your own config file path. If no config file path is specified, the default built-in configuration will be used.

### Code coverage report
    JaCoCo code coverage report is localted in target/site/jacoco/index.html


### Outpput from the CLI tool
 The tool will output the following information:

    1. Configuration (number of arrays, array size, number range minimum and maximum)
    2. Random generated number arrays per configured
    3. Extracted available numbers
    4. Extracted available numbers (by parallel approach)
    5. Largest prime number if exists, otherwise output is -1 (no prime)



