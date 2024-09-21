# Spring Boot Testing Demo Project

## Objective

This project demonstrates how to implement **Unit Tests** and **Integration Tests** in a Spring Boot application using **Spring Test** and **Testcontainers**. Additionally, it showcases the use of **JaCoCo** plugin for code coverage analysis.

## Key Features

- **Unit Testing**: Demonstrates writing unit tests to verify individual components and services.
- **Integration Testing**: Shows how to write integration tests using **Spring Test** to test application components in combination.
- **Testcontainers**: Uses Testcontainers to spin up real Docker containers for services like databases during integration testing.
- **JaCoCo Plugin**: Generates code coverage reports to track how much of the application is covered by tests.

## Technologies Used

- **Spring Boot**: Framework for building the application.
- **JUnit 5**: Testing framework for writing unit and integration tests.
- **Spring Test**: Part of Spring for integration testing.
- **Testcontainers**: Library to run Docker containers for testing purposes.
- **JaCoCo**: Plugin for measuring code coverage.
- **Maven**: For dependency management and build automation.

## Setup and Installation

1. Clone the repository.
2. Navigate to the project directory.
3. Run the tests with `mvn test`.
4. Generate the JaCoCo coverage report using `mvn jacoco:report`.
5. View the coverage report in the `target/site/jacoco/index.html`.

## Running the Tests

- **Unit Tests**: Can be executed via `mvn test`. These tests focus on individual components, such as services and repositories.
- **Integration Tests**: Use `@SpringBootTest` and **Testcontainers** to run the application with a real environment (e.g., using an actual PostgreSQL container).
  - Testcontainers are automatically configured and started before integration tests.


## Code Coverage

The project uses **JaCoCo** to monitor test coverage. To generate the coverage report, run:

```bash
mvn clean test jacoco:report



# SpringTest
Unit Test and Integration Test </br>
![Kết quả sau khi chạy Coverage](https://github.com/user-attachments/assets/a81148e7-0323-4b43-b380-3476d7cb776e)

</br>
To generate test report with Jacoco plugin type : mvn clean package

</br>

![Generate test report with Jacoco plugin](https://github.com/user-attachments/assets/d3d82ecf-86d8-4cd3-8140-a16b6c72c324)
