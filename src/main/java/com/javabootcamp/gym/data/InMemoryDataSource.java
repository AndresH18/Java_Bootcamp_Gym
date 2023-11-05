package com.javabootcamp.gym.data;

import com.javabootcamp.gym.data.model.*;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
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
import java.util.stream.Stream;

@Component
public class InMemoryDataSource implements IDataSource {
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
        set.add(entity);

        return entity;
    }

    @Override
    public <T extends IModel> T getById(int id, Class<T> type) {
        var set = getSet(type);

        var r = set.stream().filter(e -> e.getId() == id).findFirst();

        return r.orElse(null);
    }

    @Override
    public <T extends IModel> boolean update(T entity, Class<T> type) {

        var old = getById(entity.getId(), type);
        if (old == null)
            return false;

        var set = getSet(type);

        set.remove(old);
        set.add(entity);
        return true;
    }

    @Override
    public <T extends IModel> boolean delete(T entity, Class<T> type) {
        return getSet(type).remove(entity);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T extends IModel> Set<T> getSet(@NotNull Class<T> setKey) {
        return (Set<T>) data.computeIfAbsent(setKey, k -> new HashSet<>());
    }

    @Override
    public @NotNull <T extends IModel> Stream<T> search(Predicate<T> searchPredicate, Class<T> type) {
        var set = getSet(type);

        return set.stream().filter(searchPredicate);
    }

    @PostConstruct
    protected void loadData() {
        data.put(User.class, loader.loadUsers());
        data.put(TrainingType.class, loader.loadTrainingTypes());
        data.put(Trainer.class, loader.loadTrainers());
        data.put(Trainee.class, loader.loadTrainees());
        data.put(Training.class, loader.loadTrainings());

    }
}

@Component
class InMemoryDataLoader {
    private final ResourceLoader resourceLoader;
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
            }
        } catch (Exception ignored) {

        }
        return users;
    }

    Set<TrainingType> loadTrainingTypes() {
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
            }
        } catch (Exception ignored) {

        }
        return types;
    }

    Set<Trainee> loadTrainees() {
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
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return trainees;
    }

    Set<Trainer> loadTrainers() {
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
            }
        } catch (Exception ignored) {

        }
        return trainers;
    }

    Set<Training> loadTrainings() {
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
            }
        } catch (Exception ignored) {

        }
        return trainings;
    }

}
