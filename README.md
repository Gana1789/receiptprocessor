# ReceiptProcessor Spring Boot Application with H2 Database

This is a Spring Boot application that provides two REST endpoints handling receipt process with an H2 in-memory database. The application has been dockerized for easy containerized deployment.

## Prerequisites

Make sure you have the following installed on your machine:

- [Docker](https://www.docker.com/products/docker-desktop)
- [Maven](https://maven.apache.org/install.html)
- [Java 17 or later](https://adoptopenjdk.net/) (for running the application and building the project)

## Features

- Receipt Processor endpoints exposed
- H2 in-memory database used for data storage
- Dockerized for easy deployment

## Getting Started

### 1. Clone the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/your-username/receiptprocessor.git
cd receiptprocessor
```
do maven clean install
```
mvn clean install
```

dockerize the application
```
docker build -t receiptprocessor .
```
Run the application
```
docker run -p 8080:8080 receiptprocessor
```

For Api Spec use swagger endpoint

```
http://localhost:8080/swagger-ui/index.html
```


