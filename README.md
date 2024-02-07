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
The project can be accessed via the following link: GitHub Repository. The main page of the application (index.html at "http://localhost:8080/") provides a brief description of the project and includes a navigation bar with the main functionality of the application.

1. Create Trainer Profile:
   Implemented a form to create a new trainer at "http://localhost:8080/trainers/add".
2. Create Trainee Profile:
   Developed a form to create a new trainee at "http://localhost:8080/trainees/add".
3. Trainee Username and Password Matching:
   Implemented validation for trainee username and password during login at "http://localhost:8080/trainee-login".
   Users can retrieve their username and password from the View Profile page for convenience at "http://localhost:8080/trainees/edit/{id}".
4. Trainer Username and Password Matching:
   Implemented validation for trainer username and password during login at "http://localhost:8080/trainer-login".
   Users can retrieve their username and password from the View Profile page for convenience at "http://localhost:8080/trainees/edit/{id}".
5. Select Trainer Profile by Username:
   Implemented functionality to select a trainer profile by username using TrainerRepository.findByUsername() method.
6. Select Trainee Profile by Username:
   Implemented functionality to select a trainee profile by username using TraineeRepository.findByUsername() method.
7. Trainee Password Change:
   Implemented password change functionality for trainees at "http://localhost:8080/trainees/profile/{username}/change-password", accessible from the edit page of each trainee profile.
8. Trainer Password Change:
   Implemented password change functionality for trainers at "http://localhost:8080/trainers/profile/{username}/change-password", accessible from the edit page of each trainer profile.
9. Update Trainer Profile:
   Implemented functionality to update trainer profile details at "http://localhost:8080/trainers/edit/{1}", accessible from the edit page of each trainer profile.
10. Update Trainee Profile:
   Implemented functionality to update trainee profile details at "http://localhost:8080/trainees/edit/{id}", accessible from the edit page of each trainee profile.
11. Activate/De-activate Trainee:
   Implemented functionality to activate/deactivate trainee profiles at "http://localhost:8080/trainees/toggle-status/{id}", accessible from the main page listing trainees.
12. Activate/De-activate Trainer:
   Implemented functionality to activate/deactivate trainer profiles at "http://localhost:8080/trainers/toggle-status/{id}", accessible from the main page listing trainers.
13. Delete Trainee Profile by Username:
   Implemented functionality to delete trainee profiles by username at "http://localhost:8080/trainees/delete-by-username/{username}", accessible from the main page listing trainees.
14. Get Trainee Trainings List by Trainee Username and Criteria:
   Implemented functionality to retrieve trainee training lists by trainee username and criteria (from date, to date, trainer name, training type) at "http://localhost:8080/trainees/training-list", with a link accessible from the main page listing trainees.
15. Get Trainer Trainings List by Trainer Username and Criteria:
   Implemented functionality to retrieve trainer training lists by trainer username and criteria (from date, to date, trainee name) at "http://localhost:8080/trainers/training-list", with a link accessible from the main page listing trainers.
16. Add Training:
   Implemented functionality to add new training records at "http://localhost:8080/training/add", with a link accessible from the main page listing trainings.
17. Get Trainers List Not Assigned to Trainee by Trainee's Username:
   Implemented functionality to retrieve a list of trainers not assigned to a specific trainee by the trainee's username at "http://localhost:8080/trainees/training-list", with a link accessible from the main page listing trainees. The table of available trainers is shown below.
18. Update Trainee's Trainers List:
   Implemented logic in TrainingController.editTraining() at "http://localhost:8080/training/edit/{id}", as direct changes to a trainee's trainers list without updating training details are not feasible.


## Comments on Notes:

1. During Create Trainer/Trainee profile username and password should be generated as
   described in previous module.
   Implemented username and password generation as described in the previous module.
2. All functions except Create Trainer/Trainee profile. Should be executed only after
   Trainee/Trainer authentication (on this step should be checked username and password
   matching)
    This logic can be easily implemented using Spring Security, which will be covered later. 
3. Pay attention on required field validation before Create/Update action execution.
   Added validation for required fields such as firstName (@NotBlank), lastName (@NotBlank), and birthDate (@Past) 
   of the Trainee entity. Default error messages are displayed if irrelevant inputs are provided.
4. Users Table has parent-child (one to one) relation with Trainer and Trainee tables.
   Users table is not used in this project.
5. Trainees and Trainers have many to many relations.
   Trainees and Trainers have many-to-one relationships with the Training entity.
6. Activate/De-activate Trainee/Trainer profile not idempotent action.
   Implemented activation/deactivation of profiles as non-idempotent actions, where the activation status toggles with each action.
7. Delete Trainee profile is hard deleting action and bring the cascade deletion of relevant
   trainings.
   Improved Trainee's delete functionality to perform hard deletion, resulting in the 
   cascade deletion of relevant trainings associated with the deleted trainee.
8. Training duration have a number type.
   Used number type for training duration (in minutes) and date type for training date.
9. Training Date, Trainee Date of Birth have Date type.
   LocalDate is used for the trainee's date of birth to avoid unnecessary timestamps.
10. Training related to Trainee and Trainer by FK.
    Implemented foreign key relations for training with both trainees and trainers.
11. Is Active field in Trainee/Trainer profile has Boolean type.
    Utilized boolean type for the "Is Active" field in both trainee and trainer profiles.
12. Training Types table include constant list of values and could not be updated from the
    application.
    Created a constant list of values for training types using Flyway and provided functionality to retrieve all training types.
13. Each table has its own PK.
    Yes, ensured that each table has its own primary key for proper data organization and retrieval.
14. Try to imagine what are the reason behind the decision to save Training and Training Type tables separately with 
    one-to-many relation. The decision to save the Training and Training Type tables separately with a one-to-many 
    relationship can be based on several factors, such as normalization, flexibility, scalability.
15. Use transaction management to perform actions in a transaction where it necessary.
    Implemented transaction management for necessary actions in TraineeService, TrainerService, and TrainingService classes.
16. Configure Hibernate for work with DBMS that you choose.
    Configured Hibernate to work with the PostgreSQL database.
17. Cover code with unit tests. Code should contain proper logging
    Wrote unit tests for Trainee, TraineeService, and TraineeController, achieving more than 80% coverage. 
    Implemented logging using @Slf4j for TraineeService and TraineeController classes.
