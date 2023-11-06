# Gym

Spring based module, which handles gyn CRM system

## Task

1. Create DB schema according to the structure.

    ```mermaid
    erDiagram
        User ||--o| Trainee: ""
        User ||--o| Trainer: ""
        Trainee ||--o{ Training: ""
        Trainer ||--o{ Training: ""
        TT ||--o{ Trainer: ""
        TT ||--o{ Training: ""
    
        User {
            int id PK
            String firstName
            String lastName
            String username
            String password
            boolean isActive
        }
    
        Trainee {
            int id PK
            int userId FK
            Date dateOfBirth "optional"
            String address "optional"
        }
    
        Trainer {
            int id PK
            int userId FK
            int specialization FK "Training type foreign key"
        }
    
        Training {
            int id PK
            int traineeId FK
            int trainerId FK
            int typeId FK "Training type foreign key"
            String name
            Date trainingDate
            int trainingDuration
        }
    
        TT["Training Type"] {
            int id PK
            String name
        }
    ```

2. Implement three service
   classes:
    - [TraineeService.java](src/main/java/com/javabootcamp/gym/services/TraineeService.java)
        - Should support possibility to create/update/delete/select Trainee profile

    - [TrainerService.java](src/main/java/com/javabootcamp/gym/services/TrainerService.java)
        - Should support possibility to create/update/select Trainer profile

    - [TrainingService.java](src/main/java/com/javabootcamp/gym/services/TrainingService.java)
        - Should support possibility to create/select Training profile

### Notes

1. Configure spring application context based on the Spring annotation or on Java based approach.
2. Implement DAO objects for each of the domain model
   entities ([Trainer](src/main/java/com/javabootcamp/gym/data/dao/TrainerDao.java), [Trainee](src/main/java/com/javabootcamp/gym/data/dao/TraineeDao.java),
   [Training](src/main/java/com/javabootcamp/gym/data/dao/TrainingDao.java)). They should store in and retrieve data
   from a common [in-memory storage](src/main/java/com/javabootcamp/gym/data/InMemoryDataSource.java) - java map. Each
   entity should be stored under a separate namespace, so you could
   list particular entity types.
3. Storage should be implemented as a separate spring bean. Implement the ability
   to [initialize storage](src/main/java/com/javabootcamp/gym/data/InMemoryDataSource.java) with some
   prepared data from the file during the application start (use spring bean post-processing features). Path to the
   concrete file should be set using [property placeholder](src/main/resources/application.properties) and [external property file](src/main/resources/mock_data).
4. DAO with storage bean should be inserted into services beans using auto wiring. Services beans should be injected
   using constructor-based injections. The rest of the injections should be done in a setter-based way.
5. Cover code with [unit tests](src/test/java).
6. Code should contain proper logging.
7. For Trainee and Trainer create profile functionality implement username and password calculation by follow rules:
    - Username going to be calculated from Trainer/Trainee first name and last name by concatenation by using dot as a
      separator (e.g. John. Smith)
    - In the case that already exists Trainer or Trainee with the same pair of first and last name as a suffix to the
      username should be added a serial number.
    - Password should be generated as a random 10 chars length string.

## Logging

### Logging Level Information

- **TRACE**:
    - The most detailed log level.
    - Used for fine-grained or very detailed debugging information.
    - Typically used to log each step of a specific operation.
    - May produce a large number of log entries.

- **DEBUG**:
    - Used for detailed debugging information.
    - Provides more information than TRACE but less than INFO.
    - Useful for diagnosing issues during development and testing.
    - Not typically used in production systems to avoid excessive log output.

- **INFO**:
    - General information about the application's state or important events.
    - Commonly used to track application startup and shutdown, as well as significant milestones.
    - Typically useful for monitoring the overall health of the application.

- **WARN (Warning)**:
    - Indicates potential issues that should be reviewed but don't necessarily disrupt the application.
    - Typically used to log situations that may lead to errors or unexpected behavior.
    - Warn messages are usually yellow or orange in log viewers.

- **ERROR**:
    - Used for critical issues or errors that need immediate attention.
    - Represents a problem that may lead to the application's failure.
    - Often used for exception stack traces and fatal errors.
    - Red or similar colors are commonly associated with error messages in log viewers.

- **FATAL (or CRITICAL)**:
    - The most severe log level.
    - Represents a catastrophic failure or error that might cause the application to crash or become unusable.
    - Fatal errors often lead to application termination.

- **OFF**:
    - A special log level that turns off all logging. No log messages are captured or emitted when this level is used.
    - Typically used when no logging is desired or as a configuration setting to disable logging.

- **ALL**:
    - Another special log level used to enable capturing all log messages, including those at the lowest severity
      levels (
      TRACE and DEBUG).
    - Rarely used in practice, as it can lead to excessive log output.