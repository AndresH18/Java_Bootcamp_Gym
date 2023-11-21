# Database

For the database, I used Microsoft SQL Server.

## Structure

### dbo

#### Diagram

```mermaid
erDiagram
    User ||--o| Trainee: ""
    User ||--o| Trainer: ""
    Trainee ||--o{ Training: ""
    Trainer ||--o{ Training: ""
    TrainerTrainee }o--|| Trainer: ""
    TrainerTrainee }o--|| Trainee: ""
    "Training Type" ||--o{ Trainer: ""
    "Training Type" ||--o{ Training: ""

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

    TrainerTrainee {
        int id PK
        int trainer_id FK
        int trainee_id FK
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

    "Training Type" {
        int id PK
        String name
    }
```

### security

#### Diagram

```mermaid
erDiagram
    login_attempt {
        bigint id PK
        varchar(50) username
        datetime2 attempt_time
        bit success
    }

    jwt_tokens {
        bigint id PK
        varchar(50) signature "JWT signature"
        date expiration
        bit is_revoked
    }
```

## Stored Procedures

### [security].[Delete_Expired_Tokens]

Deletes tokens from the [security].[jwt_tokens] where the date is expired

```sql
CREATE PROCEDURE [security].[Delete_Expired_Tokens]
AS
BEGIN
    DELETE
    FROM [security].[jwt_tokens]
    WHERE [expiration] <= GETDATE()
END
GO
```
