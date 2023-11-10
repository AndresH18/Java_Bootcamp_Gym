# Gym CRM

Spring based module, which handles gyn CRM system

## Tasks

### Spring Core

1. Create DB schema according to the structure.

    ```mermaid
    erDiagram
        User ||--o| Trainee: ""
        User ||--o| Trainer: ""
        Trainee ||--o{ Training: ""
        Trainer ||--o{ Training: ""
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

2. Implement three service
   classes:
    - [TraineeService.java](src/main/java/com/javabootcamp/gym/services/TraineeService.java)
        - Should support possibility to create/update/delete/select Trainee profile

    - [TrainerService.java](src/main/java/com/javabootcamp/gym/services/TrainerService.java)
        - Should support possibility to create/update/select Trainer profile

    - [TrainingService.java](src/main/java/com/javabootcamp/gym/services/TrainingService.java)
        - Should support possibility to create/select Training profile

#### Notes

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
   concrete file should be set using [property placeholder](src/main/resources/application.properties)
   and [external property file](src/main/resources/mock_data).
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

## ORM - Hibernate

All annotations are defined in the `javax.persistence` package

### Entity Annotations

#### Entity Annotation

Marks a class as an entity for hibernate/ORM.

> [!IMPORTANT]
> We must ensure the object has a no-arg constructor

```java

@Entity
public class Customer {
    // fields, getters and setters
}
```

#### Id Annotation

Each entity must have a primary key which uniquely identifies it. The `@Id` annotation defines the primary key. We can
generate the identifiers in different ways which are specified by `@GeneratedValue` annotation. We can choose from our
four id generation strategies with the `strategy` element:

- `AUTO`
- `TABLE`
- `SEQUENCE`
- `IDENTITY`

If we specify `GenerationType.AUTO` the JPA provider will use any strategy it wants to generate identifiers

```java

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    // getters, setters,...
}
```

#### Table Annotation

In most cases, the name of the table in the database and the name of the entity will not be the same. In these cases, we
can specify the table name using the `@Table` annotation:

```java

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    // ...

}
```

We can also mention the schema using the schema element

```java

@Entity
@Table(name = "CUSTOMER", schema = "SHOP")
public class Customer {
    // ...
}
```

Schema name helps to distinguish one set of tables from another. If we do not use the `@Table` annotation, the name of
the
entity will be considered the name of the table.

#### Column Annotation

We can use the `@Column` annotation to mention the details of a column in the table. The `@Column` annotation has many
elements such as name, length, nullable, and unique.

```java

@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CUSTOMER_NAME", length = 50, nullable = false, unique = false)
    private String name;

    // ...
}
```

The name element specifies the name of the column in the table. The length element specifies its length. The nullable
element specifies whether the column is nullable or not, and the unique element specifies whether the column is unique.
If we don't specify this annotation, the name of the field will be considered the name of the column in the table.

#### Transient Annotation

Sometimes, we may want to make a field non-persistent. We can use the `@Transient` annotation to do so. It specifies
that
the field will not be persisted. For instance, we can calculate the age of a customer from the date of birth.

```java

@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CUSTOMER_NAME", length = 50, nullable = false)
    private String name;

    @Transient
    private Integer age;

    // ...
}
```

As a result, the field age will not be persisted to the table.

#### Enumerated Annotation

Sometimes, we may want to persist a Java enum type. We can use the `@Enumerated` annotation to specify whether the enum
should be persisted by name or by ordinal (default).

```java
public enum Gender {
    MALE, FEMALE
}

@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CUSTOMER_NAME", length = 50, nullable = false, unique = false)
    private String name;

    @Transient
    private Integer age;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // ...
}
```

### Entity Relationships

JPA defines four annotations for defining relationships between entities:

- `@OneToOne`
- `@OneToMany`
- `@ManyToOne`
- `@ManyToMany`

#### One-to-One relationships

The @OneToOne annotation is used to define a one-to-one relationship between two entities. For example, you may have a
Customer entity that contains a customer's name, email, age, gender, but you may want to maintain additional information
about a customer (such as address) in a separate Address entity. The `@OneToOne` annotation facilitates breaking down
your
data and entities this way.

The Customer class below has a single Address instance. The Address maps to a single Customer instance.

```java

@Entity
public class Customer {
    @Id
    private Long id;

    private String email;

    @Column(name = "CUSTOMER_NAME", length = 50, nullable = false, unique = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(mappedBy = "customerId")
    private Address address;
    // ...
}

@Entity
public class Address {
    @Id
    private Long id;

    private String description;

    @OneToOne
    private Customer customer;
    // ...
}
```

The JPA provider uses Address's customer field to map Address to Customer. The mapping is specified in the mappedBy
attribute in the @OneToOne annotation.

#### One-to-Many & Many-to-One relationships

The `@OneToMany` and `@ManyToOne` annotations facilitate both sides of the same relationship. Consider an example where
an
Order can have only one Customer, but a Customer may have many orders. The Order entity would define a `@ManyToOne`
relationship with Customer and the Customer entity would define a `@OneToMany` relationship with Order.

```java

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();
    // ...
}

@Entity
public class Order {
    Decimal amount;
    @Id
    private Long id;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
    // ...
}
```

In this case, the Customer class maintains a list of the orders made by that customer and the Order class maintains a
reference to its single customer. Additionally, the `@JoinColumn` specifies the name of the column in the Order table to
store the ID of the Customer.

#### Many-to-Many relationships

Finally, the `@ManyToMany` annotation facilitates a many-to-many relationship between entities. Here's a case where a
Order entity has multiple Items and Item can be included in different orders:

```java

@Entity
public class Order {
    @Id
    private Long id;

    private Date date;

    Decimal amount;

    @ManyToMany
    @JoinTable(name = "ORDER_ITEM",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private Set<Item> items = new HashSet<>();
    // ...
}

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "orders")
    private Set<Order> orders = new HashSet<>();
    // ...
}
```

In this example, we create a new table, _ORDER_ITEM_, with two columns: _ORDER_ID_ and _ITEM_ID_. Using
the `joinColumns` and
`inverseJoinColumns` attributes tells your JPA framework how to map these classes in a many-to-many relationship. The
`@ManyToMany` annotation in the Item class references the field in the Order class that manages the relationship; namely
the items property.

### Lazy Loading and Eager Loading

JPA specification defines two major strategies of loading data (Lazy and Eager):

- **EAGER** strategy is a requirement on the persistence provider runtime that data must be eagerly fetched;
- **LAZY** strategy is a hint to the persistence provider runtime that data should be fetched lazily when it is first
  time
  accessed.

The Lazy loading is commonly used to defer the loading of the attributes or associations of an entity or entities until
the point at which they are needed, meanwhile the eager loading is an opposite concept in which the attributes and
associations of an entity or entities are fetched explicitly and without any need for pointing them.

```java

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "order")
    private List<Order> orders = new ArrayList<>();
    // ...
}

@Entity
public class Order {
    Decimal amount;
    @Id
    private Long id;
    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
    // ...
}
```

### Entity Object LifeCycle

The life cycle of entity objects consists of four states: **Transient**, **Managed**, **Removed** and **Detached**:

![entity-lifecycle](https://github.com/mjc-school/MJC-School/raw/main/stage%20%233/module%20%233.%20ORM/lesson%20%233.%20Persistence%20Context/media/jpa-entity-lifecycle.png)

- **New or Transient**: an entity is new if it has just been instantiated using the new operator, and it is not
  associated
  with a persistence context. It has no persistent representation in the database and no identifier value has been
  assigned.
- **Managed (persistent)**: a managed entity instance is an instance with a persistent identity that is currently
  associated
  with a persistence context.
- **Detached**: the entity instance is an instance with a persistent identity that is no longer associated with a
  persistence
  context, usually because the persistence context was closed or the instance was evicted from the context.
- **Removed**: a removed entity instance is an instance with a persistent identity, associated with a persistence
  context, but
  scheduled for removal from the database.

- The EntityManager API allows you to change the state of an entity, or in other words, to load and store objects.

#### Making entities persistent

Once you’ve created a new entity instance (using the standard new operator) it is in new state. You can make it
persistent by associating it to either an org.hibernate.Session or a javax.persistence.EntityManager.

```java
// Example. Making an entity persistent with JPA

Customer customer=new Customer();
        customer.setName("Ivan");
        entityManager.persist(customer);


// Example. Making an entity persistent with with Hibernate API

        Customer customer=new Customer();
        customer.setName("Ivan");
        session.save(customer);
```

#### Loading Objects

It is common to want to obtain an entity along with its data (e.g. like when we need to display it in the UI).

```java
// Example. Obtaining an entity reference with its data initialized with JPA
long customerId=1;
        entityManager.find(Customer.class,new Long(customerId));

// Example. Obtaining an entity reference with its data initialized with Hibernate API
        session.get(Customer.class,new Long(customerId));

//Example. Obtaining an entity reference with its data initialized using the byId() Hibernate API
        session.byId(Customer.class).load(new Long(customerId));
```

Sometimes we need to obtain a reference to an entity without having to load its data is hugely important. The most
common case being the need to create an association between an entity and another existing entity.

```java
// Example. Obtaining an entity reference without initializing its data with JPA
Address address=new Address();
        address.setDescription("Minsk, Tolbuhina 7");
        long customerId=1;
        Customer customer=entityManager.getReference(Customer.class,customerId);
        address.setCustomer(customer);

// Example. Obtaining an entity reference without initializing its data with Hibernate API
        Address address=new Address();
        address.setDescription("Minsk, Tolbuhina 7");
        long customerId=1;
        Customer customer=session.load(Customer.class,customerId);
        address.setCustomer(customer);
```

You can reload an entity instance and its collections at any time using the refresh() method. This is useful when
database triggers are used to initialize some of the properties of the entity. Note that only the entity instance and
its collections are refreshed unless you specify REFRESH as a cascade style of any associations:

```java
// Example. Refreshing entity state with JPA
entityManager.persist(customer);
        entityManager.flush(); // force the SQL insert and triggers to run
        entityManager.refresh(customer); //re-read the state (after the trigger executes)

// Example. Refreshing entity state with Hibernate API
        Customer customer=new Customer();
        customer.setName("Ivan");
        session.save(customer);
        session.flush();
        session.refresh(customer);
```

#### Detaching objects

We can use detach() method. We pass the object to be detached as the parameter to the method:

```java
entityManager.detach(customer);
```

#### Modifying persistent objects

Entities in managed/persistent state may be manipulated by the application, and any changes will be automatically
detected and persisted when the persistence context is flushed. There is no need to call a particular method to make
your modifications persistent

```java
// Example. Modifying managed state with JPA
Customer customer=entityManage.find(Customer.class,new Long(1));
        customer.setName("Peter");
        entityManager.flush();  // changes to customer are automatically detected and persisted

// Example. Modifying managed state with Hibernate API
        Customer customer=session.byId(Customer.class ).load(new Long(1));
        customer.setName("Peter");
        session.flush();
```

#### Modifying detached objects

Many applications need to retrieve an object in one transaction, send it to the presentation layer for manipulation, and
later save the changes in a new transaction. There can be significant user think and waiting time between both
transactions. We can make use of the merge() method, for such situations. The merge method helps to bring in the
modifications made to the detached entity, in the managed entity, if any:

```java
// Example. Merging a detached entity with JPA
Customer customer=entityManage.find(Customer.class,new Long(1));
        entityManager.detach(customer);
        customer.setName("Mikle");
        customer=entityManager.merge(customer);

// Example. Merging a detached entity with Hibernate API
        Customer customer=session.byId(Customer.class ).load(new Long(1));
//Clear the Session so the person entity becomes detached
        session.clear();
        customer.setName("Mikle");
        customer=(Customer)session.merge(customer);
```

#### Deleting (removing) entit

```java
// Example. Deleting an entity with JPA
entityManager.remove(customer);

// Example. Deleting an entity with the Hibernate API
        session.delete(customer);
```

#### Checking persistent state

An application can verify the state of entities and collections in relation to the persistence context.

```java
// Example. Verifying managed state with JPA
boolean contained=entityManager.contains(customer);

// Example. Verifying managed state with Hibernate API
        boolean contained=session.contains(customer);

// Example. Verifying laziness with JPA
        PersistenceUnitUtil persistenceUnitUtil=entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        boolean customerInitialized=persistenceUnitUtil.isLoaded(customer);
        boolean customerAddressInitialized=persistenceUnitUtil.isLoaded(customer.getAddress());
        boolean customerNameInitialized=persistenceUnitUtil.isLoaded(customer,"name");

// Example. Verifying laziness with Hibernate API
        boolean customerInitialized=Hibernate.isInitialized(customer);
        boolean customerAddressInitialized=Hibernate.isInitialized(customer.getAddress());
        boolean customerNameInitialized=Hibernate.isPropertyInitialized(customer,"name");
```

#### Cascading entity state transitions

JPA allows you to propagate the state transition from a parent entity to a child. For this purpose, the JPA
javax.persistence.CascadeType defines various cascade types:

- _ALL_ - cascades all entity state transitions.
- _PERSIST_ - cascades the entity persist operation.
- _MERGE_ - cascades the entity merge operation.
- _REMOVE_ - cascades the entity remove operation.
- _REFRESH_ - cascades the entity refresh operation.
- _DETACH_ - cascades the entity detach operation.

Additionally, the CascadeType.ALL will propagate any Hibernate-specific operation, which is defined by the
org.hibernate.annotations.CascadeType enum:

- _SAVE_UPDATE_ - cascades the entity saveOrUpdate operation.
- _REPLICATE_ - cascades the entity replicate operation.
- _LOCK_ - cascades the entity lock operation.

The following examples will explain some before mentioned cascade operations using the following entities:

```java

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
    //...
}

@Entity
public class Order {
    @Id
    private Long id;

    private Date date;

    Decimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    //...
}
```

### Container and Application Managed EntityManager

Basically, there are two types of EntityManager: Container-Managed and Application-Managed.

#### Container-Managed EntityManager

Here, the container (such as a JEE Container or **Spring**) injects the EntityManager in enterprise components. In other
words, the container creates the EntityManager from the EntityManagerFactory:

```java
@PersistenceContext
private EntityManager entityManager;

@Transactional
public T save(T entity){
        entityManager.persist(entity);
        return entity;
        }
```

This also means the container is in charge of beginning the transaction, as well as committing or rolling it back.

#### Application-Managed EntityManager

An application-managed entity manager allows you to control the entity manager in application code. This entity manager
is retrieved through the EntityManagerFactory API. In order to create an EntityManager, we must explicitly call
createEntityManager() in the EntityManagerFactory:

```java
protected static EntityManagerFactory entityManagerFactory;

static {
        entityManagerFactory=Persistence.createEntityManagerFactory("com.mjc");
        }

public T save(T entity){
        EntityManager entityManager=entityManagerFactory.createEntityManager();
        try{
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        }catch(Exception e){
        entityManager.getTransaction().rollback();
        }
        entityManager.close();
        return entity;
        }
```

In this example we have more control over the flow, but also more responsibilities (e.g. we have to remember to close
the EntityManager, to explicitly call commit and rollback operations).

