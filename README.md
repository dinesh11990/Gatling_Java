
# Gatling API Performance Testing POC

## Project Overview

This project is a Proof of Concept (POC) for API performance testing using Gatling. The goal is to simulate load on the API endpoints (POST, GET, DELETE) and analyze the system's performance. This POC integrates both functional and performance tests, utilizing existing functional test scenarios.

The report generation functionality will allow MA Portal users to create and manage reports effectively. The tests focus on ensuring performance standards are met.

## Test Scope

- **Endpoints Tested:**
  - **GET**: Retrieve reports in default, JSON, and CSV formats.
  - **POST**: Authentication to obtain access tokens.
  - **DELETE**: [Include your DELETE operation details if applicable].

- **API Documentation**:  
  [API Reference Documentation](https://cfl-rndsoft.ingenico.com/display/M360/Report-builder)

## Gatling Setup

- **Version**: Gatling 3.11
- **Language**: Java
- **Environment**:  
  Set up the environment using `maven-gatling-plugin` for running simulations.

### Key Features:
- **Token Generation**:  
  A `POST` request is used to authenticate and obtain an access token for further API requests.

- **Customizations**:  
  Default Gatling reports are generated, including metrics such as response time, throughput, and status code validation.

### How to Set Up:
1. Clone the repository.
2. Make sure you have Java and Maven installed.
3. Add the required dependencies in `pom.xml` for Gatling:
   ```xml
   <dependency>
       <groupId>io.gatling.highcharts</groupId>
       <artifactId>gatling-charts-highcharts</artifactId>
       <version>3.11.0</version>
   </dependency>
   <plugin>
       <groupId>io.gatling</groupId>
       <artifactId>gatling-maven-plugin</artifactId>
       <version>4.2.9</version>
       <configuration>
           <runMultipleSimulations>true</runMultipleSimulations>
       </configuration>
   </plugin>
   ```

## Test Execution

To execute the tests, you need to specify the simulation class or enable multiple simulations:

### Option 1: Run Specific Simulation
```bash
mvn gatling:test -Dgatling.simulationClass=DR_GET_ID_Endpoint.Case_200.GETID_Simulation
```

### Option 2: Run Multiple Simulations
Ensure `runMultipleSimulations` is enabled in your `pom.xml`, and then run:
```bash
mvn gatling:test
```

### Scenarios

The following scenarios are executed:
1. **Get Report In Default Format**:
   - Sends a GET request using the access token.
   - Validates the response with status 200.

2. **Get Report In JSON Format**:
   - Fetches a report in JSON format.
   - Status validation and response time checks.

3. **Get Report In CSV Format**:
   - Fetches a report in CSV format.

Each scenario pauses for 5 seconds between requests to simulate realistic user behavior.

### Virtual Users and Duration
- Each scenario runs with **10 virtual users** concurrently.

## Results & Analysis

- **Metrics Captured**:
  - Response Time
  - Throughput
  - Status Code Validation (200 OK)

- **Reports**:  
  Reports are automatically generated in `target/gatling/results`, including detailed statistics and graphs.

## GitLab Integration

This project is integrated with GitLab CI/CD for continuous performance testing. The generated reports are stored as build artifacts and can be reviewed within the pipeline.

### Key GitLab CI/CD Features:
- **Automated Test Execution**: Runs on each commit.
- **Report Generation**: Automatically uploads reports.

## Conclusion & Recommendations

- **System Performance**: The system met the expected performance requirements under the load of 10 virtual users. The response times were within acceptable limits.
- **Recommendations**: Further testing with a higher number of virtual users is recommended to evaluate scalability.

## Next Steps

- Scale the number of virtual users to simulate a more realistic production load.
- Include additional endpoints in performance testing as required.
