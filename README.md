# basicCrud

### This project is a simple CRUD (Create, Read, Update, Delete) application for managing locations. It provides a RESTful API for performing operations on location data.

## Table of Contents
- [Overview](#overview)
- [Technologies](#technologies)
- [Setup](#setup)
- [Usage](#usage)
- [Testing](#testing)

## Overview

The Location Service application allows users to:
- Create new locations
- Retrieve existing locations
- Update location details
- Delete locations

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (for development)
- Maven
- PostgresSql
- JUnit 4 (LocationServiceTest)
- JUnit 5 (DepartmentServiceTest)
- Mockito

## Setup

Install postgresSql dependency and set up the configuration for the db in application properties.
- spring.datasource.url=jdbc:postgresql://localhost:5432/crudBasic (replace crudBasic with your existing or create new with the same name in postgresSql)
- spring.datasource.username=USERNAME(postgresSql)
- spring.datasource.password=PASSWORD(postgresSql)

### Prerequisites

Ensure you have the following installed on your system:
- Java 11
- Maven

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/location-service.git
    cd location-service
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

### Configuration

The application uses an H2 in-memory database by default. You can change the database configuration in the `application.properties` file.

## Usage

1. Start the application:
    ```sh
    mvn spring-boot:run
    ```

2. The application will be available at `http://localhost:8080`.

### API Endpoints
#### Location
- `GET /api/location` - Retrieve all locations
- `GET /api/location/id/{id}` - Retrieve a location by ID
- `GET /api/location/name/{id}` - Retrieve a location by NAME
- `POST /api/location` - Create a new location
- `PUT /api/location/{id}` - Update an existing location
- `DELETE /api/location/id/{id}` - Delete a location by ID
#### Department 
- `GET /api/department` - Retrieve all departments
- `GET /api/department/id/{id}` - Retrieve a department by ID
- `GET /api/department/name/{id}` - Retrieve a department by NAME
- `POST /api/department` - Create a new department
- `PUT /api/department/{id}` - Update an existing department
- `DELETE /api/department/id/{id}` - Delete a department by ID


## Example Request
### Location
Retrieve all locations
```shell
curl -X GET http://localhost:8080/api/location 
```
Retrieve location by id
```shell
curl -X GET http://localhost:8080/api/location/id/846438fb-44a1-46ee-9e27-ac6277ce2c51
```
Create Location
```shell
curl -X POST http://localhost:8080/api/location -H "Content-Type: application/json" -d '{"name": "New Location", "department":{"name":"HR"}}'
```
Delete location by ID
```shell
curl -X DELETE http://localhost:8080/api/location/delete/id/846438fb-44a1-46ee-9e27-ac6277ce2c51
```
### Department
Retrieve all departments
```shell
curl -X GET http://localhost:8080/api/department
```
Retrieve department by id
```shell
curl -X GET http://localhost:8080/api/department/id/28eb0b2e-bde8-4a13-a2b4-cad07fa5e094
```
Create Department
```shell
curl -X POST http://localhost:8080/api/department -H "Content-Type: application/json" -d '{"name": "Backend"}'
```
Delete department by ID
```shell
curl -X DELETE http://localhost:8080/api/department/delete/id/28cdffe8-455a-4f97-bd5a-a7f7dc8cc2
```
### Location
- Invoke-WebRequest -Uri "http://localhost:8080/api/location" -Method Get
- Invoke-WebRequest -Uri "http://localhost:8080/api/location/id/846438fb-44a1-46ee-9e27-ac6277ce2c51" -Method Get
- Invoke-WebRequest -Uri "http://localhost:8080/api/location/name/Frankfurt" -Method Get

### Department
- Invoke-WebRequest -Uri "http://localhost:8080/api/department" -Method Get
- Invoke-WebRequest -Uri "http://localhost:8080/api/department/id/28eb0b2e-bde8-4a13-a2b4-cad07fa5e094" -Method Get
- Invoke-WebRequest -Uri "http://localhost:8080/api/department/name/hr" -Method Get

## Testing
Unit tests. Both department and location controller and service tests.

Run all tests
1. Open intellij
2. Right side - click on M (Maven)
3. Double-click on basicCrud
4. Double-click on Lifecycle
5. Double-click on test