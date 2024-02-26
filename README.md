# Gym CRM System

## Overview

This Spring MVC project is a designed to handle a Gym CRM (Customer Relationship Management) system. The system includes functionality for managing trainee profiles, trainer profiles, and training sessions.

## Database Schema

The database schema for the Gym CRM system is designed to accommodate the following structure:

![img.png](img.png)

Please refer to the attached schema image for a visual representation of the database structure.

## Modules

### Trainee Service

The Trainee Service class provides functionality to manage trainee profiles. This includes the ability to create, update, delete, and select trainee profiles.

### Trainer Service
The Trainer Service class is responsible for managing trainer profiles. It offers functionality to create, update, and select trainer profiles.

### Training Service
The Training Service class supports the creation and selection of training profiles. It facilitates the management of training sessions within the Gym CRM system.

## Getting Started

To integrate this module into your project, follow these steps:

1. Clone the repository: `git clone https://github.com/aigulsharip/GymCRMCoreMVC.git`
2. Import the project into your preferred IDE.
3. Configure the database connection in the `application.properties` file.
4. Build and run the application.

## Dependencies

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/html/main.html) (for development/testing)
- Add any additional dependencies...


## Project Completion Comments:
The project can be accessed via the following link: https://github.com/aigulsharip/GymCRMCoreMVC.git. 
Branch: spring-boot-task from rest-task 

All the tasks (No.1-17) has been completed fully.
1. Convert existing application to be Spring boot Application.
    This project has been already created and run as Spring Boot Application, so no conversion is needed.
2. Enable actuator.
     Implement a few custom health indicators.
     * Few custom health indicators has been implemented in ExternalServiceHealthIndicator and CustomHealthIndicator. 
     Implement a few custom metrics using Prometheus.
     * Few custom metrics has been implemented in CustomMetricsController using CustomMetricsCollector.
3. Implement support for different environments (local, dev, stg, prod). Use Spring profiles.
   Profiles for dev and prod environment has been created
   different properties for server port and database configurations has been indicated in 
   application-dev.properties and application-prod.properties

## Comments on Notes:

1. Cover code with unit tests. Code should contain proper logging.
   Wrote unit tests for Trainee, TraineeService, and TraineeController, achieving more than 80% coverage.
   Logging was implemented in LoggingInterceptor and WebMvcConfig classes.
   Implemented logging using @Slf4j for all service and controller classes.
2. Pay attention that each environment - different db properties.
   Different db configs indicatedd for each environment
3. All functions except Create Trainer/Trainee profile. Should be executed only after Trainee/Trainer authentication (on this step should be checked
username and password matching).
   Will be completed in Spring Security module.