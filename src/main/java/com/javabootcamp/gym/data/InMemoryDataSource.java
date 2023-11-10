package com.javabootcamp.gym.data;

import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javabootcamp.gym.data.IDataSource.MemoryModels.*;

@Component
public class InMemoryDataSource implements IDataSource {
    private final Logger logger = LoggerFactory.getLogger(InMemoryDataSource.class);
    private final Map<Class<? extends IModel>, Set<?>> data;
    private InMemoryDataLoader loader;

    public InMemoryDataSource() {
        data = new HashMap<>();
    }

    @Autowired
    InMemoryDataSource(InMemoryDataLoader loader) {
        this();
        this.loader = loader;
    }

    protected InMemoryDataSource(Map<Class<? extends IModel>, Set<?>> data) {
        this.data = data;
    }

    @NotNull
    @Override
    public <T extends IModel> T create(@NotNull T entity, @NotNull Class<T> type) {
        var set = getSet(type);

        entity.setId(set.size() + 1);
        set.add(new Entity<>(entity));

        logger.trace("create: entity type={}, id={}", type.getName(), entity.getId());

        return entity;
    }

    @Override
    public <T extends IModel> T getById(int id, @NotNull Class<T> type) {
        logger.trace("getById: type={}, id={}", id, type);
        var set = getSet(type);

        var r = set.stream().filter(e -> e.get().getId() == id).findFirst();

        return r.map(Entity::get).orElse(null);
    }

    @Override
    public <T extends IModel> boolean update(final @NotNull T entity, @NotNull Class<T> type) {
        logger.trace("update: type={}, id={}", type.getName(), entity.getId());
//        var old = getById(entity.getId(), type);
//        if (old == null)
//            return false;
//
//        var set = getSet(type);
//
//        set.remove(new Entity<>(entity));
//        set.add(new Entity<>(entity));
//        return true;

        var t = new Entity<>(entity);
        var set = getSet(type);
        if (!set.remove(t))
            return false;

        set.add(t);

        return true;
    }

    @Override
    public <T extends IModel> boolean delete(@NotNull T entity, @NotNull Class<T> type) {
        logger.trace("delete: type={}, id={}", type.getName(), entity.getId());

        var t = new Entity<>(entity);
        return getSet(type).remove(t);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T extends IModel> Set<Entity<T>> getSet(@NotNull Class<T> setKey) {
        logger.trace("Retrieving set for {}", setKey.getName());

//        Set<Entity<T>> s = (Set<Entity<T>>) data.get(setKey);
//        if (s == null) {
//            s = new HashSet<>();
//            data.put(setKey, s);
//        }
//        return s;
        return (Set<Entity<T>>) data.computeIfAbsent(setKey, k -> new HashSet<>());
    }

    @Override
    public @NotNull <T extends IModel> Stream<T> search(Predicate<T> searchPredicate, Class<T> type) {
        logger.trace("search: ");
        var set = getSet(type);

        return set.stream().filter(e -> searchPredicate.test(e.get()))
                .map(Entity::get);
    }

    @PostConstruct
    protected void loadData() {
        logger.trace("loadData: loading data from files");
        var users = loader.loadUsers().parallelStream().map(Entity<User>::new).collect(Collectors.toSet());
        var trainingTypes = loader.loadTrainingTypes().parallelStream().map(Entity<TrainingType>::new).collect(Collectors.toSet());
        var trainers = loader.loadTrainers().parallelStream().map(Entity<Trainer>::new).collect(Collectors.toSet());
        var trainees = loader.loadTrainees().parallelStream().map(Entity<Trainee>::new).collect(Collectors.toSet());
        var trainings = loader.loadTrainings().parallelStream().map(Entity<Training>::new).collect(Collectors.toSet());

        data.put(User.class, users);
        data.put(TrainingType.class, trainingTypes);
        data.put(Trainer.class, trainers);
        data.put(Trainee.class, trainees);
        data.put(Training.class, trainings);
    }

    public static class Entity<T extends IModel> {
        private final T t;

        public Entity(T t) {
            this.t = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entity<?> entity1 = (Entity<?>) o;

            return t.getId() == entity1.t.getId();
        }

        @Override
        public int hashCode() {
            return t.getId();
        }

        public T get() {
            return t;
        }
    }
}


@Component
class InMemoryDataLoader {
    private final ResourceLoader resourceLoader;
    private final Logger logger = LoggerFactory.getLogger(InMemoryDataLoader.class);
    @Value("${data.mock.user}")
    private String userResourceName;
    @Value("${data.mock.training_type}")
    private String trainingTypeResourceName;
    @Value("${data.mock.trainee}")
    private String traineeResourceName;
    @Value("${data.mock.trainer}")
    private String trainerResourceName;
    @Value("${data.mock.training}")
    private String trainingResourceName;

    @Autowired
    public InMemoryDataLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    Set<User> loadUsers() {
        logger.trace("Loading Users");
        var users = new HashSet<User>();
        var resource = resourceLoader.getResource(userResourceName);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                int id = Integer.parseInt(row[0]);
                String firstName = row[1];
                String lastName = row[2];
                String username = row[3];
                String password = row[4];
                boolean isActive = Boolean.parseBoolean(row[5]);

                var user = new User(id, firstName, lastName, username, password, isActive);

                users.add(user);

                logger.trace("Loaded user, id={}", id);
            }
        } catch (Exception e) {
            logger.error("Error loading users", e);
        }
        return users;
    }

    Set<TrainingType> loadTrainingTypes() {
        logger.trace("Loading Training Types");
        var types = new HashSet<TrainingType>();
        var resource = resourceLoader.getResource(trainingTypeResourceName);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                int id = Integer.parseInt(row[0]);
                String name = row[1];

                var type = new TrainingType(id, name);

                types.add(type);
                logger.trace("Loaded training type, id={}", id);
            }
        } catch (Exception e) {
            logger.error("Error loading training types", e);
        }
        return types;
    }

    Set<Trainee> loadTrainees() {
        logger.trace("Loading Trainees");
        var trainees = new HashSet<Trainee>();
        var formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        var resource = resourceLoader.getResource(traineeResourceName);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                int id = Integer.parseInt(row[0]);
                int userId = Integer.parseInt(row[1]);
                LocalDate dateOfBirth = LocalDate.parse(row[2], formatter);
                String address = row[3];

                var trainee = new Trainee(id, userId, dateOfBirth, address);

                trainees.add(trainee);
                logger.trace("Loaded trainee, id={}", id);
            }
        } catch (Exception e) {
            logger.error("Error loading trainees", e);
        }
        return trainees;
    }

    Set<Trainer> loadTrainers() {
        logger.trace("Loading Trainers");
        var trainers = new HashSet<Trainer>();

        var resource = resourceLoader.getResource(trainerResourceName);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                int id = Integer.parseInt(row[0]);
                int userId = Integer.parseInt(row[1]);
                int specializationId = Integer.parseInt(row[2]);

                var trainee = new Trainer(id, userId, specializationId);

                trainers.add(trainee);
                logger.trace("Loaded trainer, id={}", id);
            }
        } catch (Exception e) {
            logger.error("Error loading trainers", e);

        }
        return trainers;
    }

    Set<Training> loadTrainings() {
        logger.trace("Loading Trainings");
        var trainings = new HashSet<Training>();
        var formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        var resource = resourceLoader.getResource(trainingResourceName);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                int id = Integer.parseInt(row[0]);
                int traineeId = Integer.parseInt(row[1]);
                int trainerId = Integer.parseInt(row[2]);
                int trainingTypeId = Integer.parseInt(row[3]);
                String name = row[4];
                LocalDate date = LocalDate.parse(row[5], formatter);
                int duration = Integer.parseInt(row[6]);

                var training = new Training(id, traineeId, trainerId, trainingTypeId, name, date, duration);

                trainings.add(training);
                logger.trace("Loaded training, id={}", id);
            }
        } catch (Exception e) {
            logger.error("Error loading trainings", e);
        }
        return trainings;
    }
}

//class MemoryModels {
//    private MemoryModels() {
//    }
//
//    static class User implements IModel {
//
//        private Integer id;
//        private String firstName;
//        private String lastName;
//        @NotNull
//        private String username = "";
//        @NotNull
//        private String password = "";
//        @Column(name = "is_active")
//        private boolean isActive;
//
//        public User(int id, @NotNull String firstName, @NotNull String lastName, @NotNull String username, @NotNull String password, boolean isActive) {
//            this.id = id;
//            this.firstName = firstName;
//            this.lastName = lastName;
//            this.username = username;
//            this.password = password;
//            this.isActive = isActive;
//        }
//
//        public User(@NotNull String firstName, @NotNull String lastName) {
//            this.firstName = firstName;
//            this.lastName = lastName;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            User user = (User) o;
//
//            if (isActive != user.isActive) return false;
//            if (!Objects.equals(id, user.id)) return false;
//            if (!firstName.equals(user.firstName)) return false;
//            if (!lastName.equals(user.lastName)) return false;
//            if (!username.equals(user.username)) return false;
//            return password.equals(user.password);
//        }
//
//        @Override
//        public int hashCode() {
//            int result = id != null ? id.hashCode() : 0;
//            result = 31 * result + firstName.hashCode();
//            result = 31 * result + lastName.hashCode();
//            result = 31 * result + username.hashCode();
//            result = 31 * result + password.hashCode();
//            result = 31 * result + (isActive ? 1 : 0);
//            return result;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        @Override
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public @NotNull String getFirstName() {
//            return firstName;
//        }
//
//        public void setFirstName(@NotNull String firstName) {
//            this.firstName = firstName;
//        }
//
//        public @NotNull String getLastName() {
//            return lastName;
//        }
//
//        public void setLastName(@NotNull String lastName) {
//            this.lastName = lastName;
//        }
//
//        public @NotNull String getUsername() {
//            return username;
//        }
//
//        public void setUsername(@NotNull String username) {
//            this.username = username;
//        }
//
//        public @NotNull String getPassword() {
//            return password;
//        }
//
//        public void setPassword(@NotNull String password) {
//            this.password = password;
//        }
//
//        public boolean isActive() {
//            return isActive;
//        }
//
//        public void setActive(boolean active) {
//            isActive = active;
//        }
//    }
//
//    static class Trainee implements IModel {
//
//        private int id;
//        private int userId;
//        @NotNull
//        private LocalDate dateOfBirth;
//        @NotNull
//        private String address;
//
//        public Trainee(int id, int userId, @NotNull LocalDate dateOfBirth, @NotNull String address) {
//            this.id = id;
//            this.userId = userId;
//            this.dateOfBirth = dateOfBirth;
//            this.address = address;
//        }
//
//        public Trainee(int userId, @NotNull LocalDate dateOfBirth, @NotNull String address) {
//            this.userId = userId;
//            this.dateOfBirth = dateOfBirth;
//            this.address = address;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Trainee trainee = (Trainee) o;
//
//            if (id != trainee.id) return false;
//            if (!dateOfBirth.equals(trainee.dateOfBirth)) return false;
//            return address.equals(trainee.address);
//        }
//
//        @Override
//        public int hashCode() {
//            int result = id;
//            result = 31 * result + dateOfBirth.hashCode();
//            result = 31 * result + address.hashCode();
//            return result;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        @Override
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public @NotNull LocalDate getDateOfBirth() {
//            return dateOfBirth;
//        }
//
//        public void setDateOfBirth(@NotNull LocalDate dateOfBirth) {
//            this.dateOfBirth = dateOfBirth;
//        }
//
//        public @NotNull String getAddress() {
//            return address;
//        }
//
//        public void setAddress(@NotNull String address) {
//            this.address = address;
//        }
//
//        public int getUserId() {
//            return userId;
//        }
//
//        public void setUserId(int userId) {
//            this.userId = userId;
//        }
//    }
//
//    static class Trainer implements IModel {
//        private int id;
//
//        private int userId;
//
//        private int specializationId;
//
//        public Trainer(int id, int userId, int specializationId) {
//            this.id = id;
//            this.userId = userId;
//            this.specializationId = specializationId;
//        }
//
//        public Trainer(int userId, int specializationId) {
//            this.userId = userId;
//            this.specializationId = specializationId;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Trainer trainer = (Trainer) o;
//
//            if (id != trainer.id) return false;
//            if (userId != trainer.userId) return false;
//            return specializationId == trainer.specializationId;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = id;
//            result = 31 * result + userId;
//            result = 31 * result + specializationId;
//            return result;
//        }
//
//        @Override
//        public int getId() {
//            return id;
//        }
//
//        @Override
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public int getSpecializationId() {
//            return specializationId;
//        }
//
//        public void setSpecializationId(int specializationId) {
//            this.specializationId = specializationId;
//        }
//
//        public int getUserId() {
//            return userId;
//        }
//
//        public void setUserId(int userId) {
//            this.userId = userId;
//        }
//    }
//
//    static class TrainingType implements IModel {
//        private int id;
//        @NotNull
//        private String name;
//
//        public TrainingType(int id, @NotNull String name) {
//            this.id = id;
//            this.name = name;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            TrainingType that = (TrainingType) o;
//
//            if (id != that.id) return false;
//            return name.equals(that.name);
//        }
//
//        @Override
//        public int hashCode() {
//            int result = id;
//            result = 31 * result + name.hashCode();
//            return result;
//        }
//
//        @Override
//        public int getId() {
//            return id;
//        }
//
//        @Override
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public @NotNull String getName() {
//            return name;
//        }
//
//        public void setName(@NotNull String name) {
//            this.name = name;
//        }
//    }
//
//    static class Training implements IModel {
//        private int id;
//        private int duration;
//        @NotNull
//        private String name;
//        @NotNull
//        private LocalDate date;
//        private int traineeId;
//        private int trainerId;
//        private int trainingTypeId;
//
//        public Training(int id, int traineeId, int trainerId, int trainingTypeId, @NotNull String name, @NotNull LocalDate date, int duration) {
//            this.id = id;
//            this.traineeId = traineeId;
//            this.trainerId = trainerId;
//            this.trainingTypeId = trainingTypeId;
//            this.name = name;
//            this.date = date;
//            this.duration = duration;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Training training = (Training) o;
//
//            if (id != training.id) return false;
//            if (duration != training.duration) return false;
//            if (traineeId != training.traineeId) return false;
//            if (trainerId != training.trainerId) return false;
//            if (trainingTypeId != training.trainingTypeId) return false;
//            if (!name.equals(training.name)) return false;
//            return date.equals(training.date);
//        }
//
//        @Override
//        public int hashCode() {
//            int result = id;
//            result = 31 * result + duration;
//            result = 31 * result + name.hashCode();
//            result = 31 * result + date.hashCode();
//            result = 31 * result + traineeId;
//            result = 31 * result + trainerId;
//            result = 31 * result + trainingTypeId;
//            return result;
//        }
//
//        @NotNull
//        public String getName() {
//            return name;
//        }
//
//        public void setName(@NotNull String name) {
//            this.name = name;
//        }
//
//        @NotNull
//        public LocalDate getDate() {
//            return date;
//        }
//
//        public void setDate(@NotNull LocalDate date) {
//            this.date = date;
//        }
//
//        public int getDuration() {
//            return duration;
//        }
//
//        public void setDuration(int duration) {
//            this.duration = duration;
//        }
//
//        @Override
//        public int getId() {
//            return id;
//        }
//
//        @Override
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public int getTraineeId() {
//            return traineeId;
//        }
//
//        public void setTraineeId(int traineeId) {
//            this.traineeId = traineeId;
//        }
//
//        public int getTrainerId() {
//            return trainerId;
//        }
//
//        public void setTrainerId(int trainerId) {
//            this.trainerId = trainerId;
//        }
//
//        public int getTrainingTypeId() {
//            return trainingTypeId;
//        }
//
//        public void setTrainingTypeId(int trainingTypeId) {
//            this.trainingTypeId = trainingTypeId;
//        }
//    }
//}

