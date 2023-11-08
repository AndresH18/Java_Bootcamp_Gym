package com.javabootcamp.gym.data;

import com.javabootcamp.gym.data.model.*;
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
