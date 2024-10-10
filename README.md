# Student Management System

## Overview

This project is a simple Student Management System developed using Java Servlets, JDBC, and Jakarta EE technologies. It allows you to perform basic CRUD (Create, Read, Update, Delete) operations on student records. 

## Features

- Add new students
- Retrieve student information
- Update existing student records
- Delete student records

## Technologies Used

- Java EE (Jakarta)
- JDBC for database connectivity
- JSON-B for JSON processing
- Log4j for logging
- MySQL (or any other database of your choice)

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/student_management_system.git
   cd student_management_system
   ```

2. **Database Setup**:
   - Create a database for the application (e.g., `student_db`).
   - Execute the SQL scripts to create the `Student` table as defined in the application.

3. **Configuration**:
   - Update the `web.xml` or appropriate configuration files to set your database connection parameters (JDBC URL, username, password).

4. **Logging Configuration**:
   - The project uses **Log4j** for logging. Make sure to include the Log4j dependency in your `pom.xml` or build configuration.
   - Configure your logging by placing the `log4j2.xml` configuration file in the `src/main/resources` directory. 
   - Here’s an example `log4j2.xml` configuration to log to a file:
     ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <Configuration status="WARN">
         <Appenders>
             <File name="FileLogger" fileName="logs/app.log" append="true">
                 <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %level %logger{36} - %msg%n"/>
             </File>
         </Appenders>
         <Loggers>
             <Root level="info">
                 <AppenderRef ref="FileLogger"/>
             </Root>
         </Loggers>
     </Configuration>
     ```

## Running the Application

- Deploy the application on a servlet container (e.g., Apache Tomcat).
- Access the application via your web browser at `http://localhost:8080/student_management_system`.

## Logging

The application uses **Log4j** for logging activities. All log messages will be saved in the specified log file as per the Log4j configuration. Make sure that the directory for logs is created and accessible by the application to avoid permission issues.

### Log Levels

- `INFO`: General information about application processes.
- `DEBUG`: Detailed information for debugging purposes.
- `ERROR`: Information about errors that occur during execution.
