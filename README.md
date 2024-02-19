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
The project can be accessed via the following link: https://github.com/aigulsharip/GymCRMCoreMVC.git

All the tasks (No.1-17) has been completed fully.

## Comments on Notes:
1. During Create Trainer/Trainee profile username and password should be generated as
   described in previous modules.
   Implemented username and password generation as described in the previous module.
2. Not possible to register as a trainer and trainee both.
   Yes, this logic was implemented in RegistrationLoginService class and used in RegistrationLoginController
3. All functions except Create Trainer/Trainee profile. Should be executed only after
   Trainee/Trainer authentication (on this step should be checked username and password
   matching).
   Will be completed in Spring Security module.
4. Implement required validation for each endpoint.
   Added validation for required fields, such as firstName (@NotBlank), lastName (@NotBlank), and birthDate (@Past)
   of the Trainee entity. Default error messages are displayed if irrelevant inputs are provided.
5. Users Table has parent-child (one to one) relation with Trainer and Trainee tables.
   This table was not used at all.
6. Training functionality does not include delete/update possibility via REST
    Yes, delete/update functionality was not implemented.
7. Username cannot be changed.
    Generated username can not be changed
8. Trainees and Trainers have many to many relations.
   Trainees and Trainers have many to many relations.
9. Activate/De-activate Trainee/Trainer profile not idempotent action.
   Implemented activation/deactivation of profiles as non-idempotent actions, where the activation status toggles with each action.
10. Delete Trainee profile is hard deleting action and bring the cascade deletion of relevant
    trainings.
    Improved Trainee's delete functionality to perform hard deletion, resulting in the
    cascade deletion of relevant trainings associated with the deleted trainee.
11. Training duration have a number type. 
    Used number type for training duration (in minutes) and date type for training date.
12. Training Date, Trainee Date of Birth have Date type.
    LocalDate is used for the trainee's date of birth to avoid unnecessary timestamps.
13. Is Active field in Trainee/Trainer profile has Boolean type.
    Utilized boolean type for the "Is Active" field in both trainee and trainer profiles.
14. Training Types table include constant list of values and could not be updated from the
    application.
    Created a constant list of values for training types using Flyway and provided functionality to retrieve all training types.
15. Implement error handling for all endpoints.
    Error handling was implemented in GlobalExceptionHandler class.
16. Cover code with unit tests.
    Wrote unit tests for Trainee, TraineeService, and TraineeController, achieving more than 80% coverage.
    Implemented logging using @Slf4j for TraineeService and TraineeController classes.
17. Two levels of logging should be implemented:
    Transaction level (generate transactionId by which you can track all operations
    for this transaction the same transactionId can later be passed to downstream
    services)
    Specific rest call details (which endpoint was called, which request came and the
    service response - 200 or error and response message).
    Logging was implemented in LoggingInterceptor and WebMvcConfig classes.
    Implemented logging using @Slf4j for all service and controller classes. 
18. Implement error handling.
    Error handling was implemented in GlobalExceptionHandler class.
19. Document methods in RestController file(s) using Swagger 2 annotations
    Swagger documentation available here: http://localhost:8080/swagger-ui/index.html
    OpenAPI descriptions at: http://localhost:8080/api-docs
    Custom description can be added to our API using a couple of OpenAPI-specific annotations: @Operation and @ApiResponses
    Generated documentation for TrainingTypeController using these annotations.