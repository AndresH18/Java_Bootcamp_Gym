package com.javabootcamp.gym.data;

import jakarta.persistence.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.javabootcamp.gym.data.IDataSource.MemoryModels.*;

/**
 * The IDataSource interface provides methods for creating, retrieving, updating, and deleting
 * entities of a specific type.
 */
@Deprecated
public interface IDataSource {
    /**
     * Creates a new entity of the specified type.
     *
     * @param entity The entity to create.
     * @param type   The class representing the entity's type.
     * @param <T>    The type of the entity, which must implement the IModel interface.
     * @return The created entity.
     */
    @NotNull <T extends IModel> T create(@NotNull T entity, @NotNull Class<T> type);

    /**
     * Retrieves an entity of the specified type by its unique identifier.
     *
     * @param id   The unique identifier of the entity.
     * @param type The class representing the entity's type.
     * @param <T>  The type of the entity, which must implement the IModel interface.
     * @return The retrieved entity, or null if not found.
     */

    @Nullable <T extends IModel> T getById(int id, @NotNull Class<T> type);

    /**
     * Updates an existing entity of the specified type.
     *
     * @param entity The entity to update.
     * @param type   The class representing the entity's type.
     * @param <T>    The type of the entity, which must implement the IModel interface.
     * @return True if the update was successful, false otherwise.
     */
    <T extends IModel> boolean update(@NotNull T entity, @NotNull Class<T> type);

    /**
     * Deletes an existing entity of the specified type.
     *
     * @param entity The entity to delete.
     * @param type   The class representing the entity's type.
     * @param <T>    The type of the entity, which must implement the IModel interface.
     * @return True if the deletion was successful, false otherwise.
     */
    <T extends IModel> boolean delete(@NotNull T entity, @NotNull Class<T> type);

    @NotNull
    default <T extends IModel> Stream<T> search(Predicate<T> searchPredicate, Class<T> type) {
        return Stream.empty();
    }

    /**
     * Nested Memory Model Clases. They were the version used when the InMemoryDataSource was used.
     *
     * <p>
     * Were moved here to not have to delete them
     * </p>
     */
    class MemoryModels {
        private MemoryModels() {
        }

        public interface IModel {
            int getId();
            void setId(int id);
        }
        public static class User implements IModel {

            private Integer id;
            private String firstName;
            private String lastName;
            @NotNull
            private String username = "";
            @NotNull
            private String password = "";
            @Column(name = "is_active")
            private boolean isActive;

            public User(int id, @NotNull String firstName, @NotNull String lastName, @NotNull String username, @NotNull String password, boolean isActive) {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.username = username;
                this.password = password;
                this.isActive = isActive;
            }

            public User(@NotNull String firstName, @NotNull String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                User user = (User) o;

                if (isActive != user.isActive) return false;
                if (!Objects.equals(id, user.id)) return false;
                if (!firstName.equals(user.firstName)) return false;
                if (!lastName.equals(user.lastName)) return false;
                if (!username.equals(user.username)) return false;
                return password.equals(user.password);
            }

            @Override
            public int hashCode() {
                int result = id != null ? id.hashCode() : 0;
                result = 31 * result + firstName.hashCode();
                result = 31 * result + lastName.hashCode();
                result = 31 * result + username.hashCode();
                result = 31 * result + password.hashCode();
                result = 31 * result + (isActive ? 1 : 0);
                return result;
            }

            public int getId() {
                return id;
            }

            @Override
            public void setId(int id) {
                this.id = id;
            }

            public @NotNull String getFirstName() {
                return firstName;
            }

            public void setFirstName(@NotNull String firstName) {
                this.firstName = firstName;
            }

            public @NotNull String getLastName() {
                return lastName;
            }

            public void setLastName(@NotNull String lastName) {
                this.lastName = lastName;
            }

            public @NotNull String getUsername() {
                return username;
            }

            public void setUsername(@NotNull String username) {
                this.username = username;
            }

            public @NotNull String getPassword() {
                return password;
            }

            public void setPassword(@NotNull String password) {
                this.password = password;
            }

            public boolean isActive() {
                return isActive;
            }

            public void setActive(boolean active) {
                isActive = active;
            }
        }

        public static class Trainee implements IModel {

            private int id;
            private int userId;
            @NotNull
            private LocalDate dateOfBirth;
            @NotNull
            private String address;

            public Trainee(int id, int userId, @NotNull LocalDate dateOfBirth, @NotNull String address) {
                this.id = id;
                this.userId = userId;
                this.dateOfBirth = dateOfBirth;
                this.address = address;
            }

            public Trainee(int userId, @NotNull LocalDate dateOfBirth, @NotNull String address) {
                this.userId = userId;
                this.dateOfBirth = dateOfBirth;
                this.address = address;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Trainee trainee = (Trainee) o;

                if (id != trainee.id) return false;
                if (!dateOfBirth.equals(trainee.dateOfBirth)) return false;
                return address.equals(trainee.address);
            }

            @Override
            public int hashCode() {
                int result = id;
                result = 31 * result + dateOfBirth.hashCode();
                result = 31 * result + address.hashCode();
                return result;
            }

            public int getId() {
                return id;
            }

            @Override
            public void setId(int id) {
                this.id = id;
            }

            public @NotNull LocalDate getDateOfBirth() {
                return dateOfBirth;
            }

            public void setDateOfBirth(@NotNull LocalDate dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
            }

            public @NotNull String getAddress() {
                return address;
            }

            public void setAddress(@NotNull String address) {
                this.address = address;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        public static class Trainer implements IModel {
            private int id;

            private int userId;

            private int specializationId;

            public Trainer(int id, int userId, int specializationId) {
                this.id = id;
                this.userId = userId;
                this.specializationId = specializationId;
            }

            public Trainer(int userId, int specializationId) {
                this.userId = userId;
                this.specializationId = specializationId;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Trainer trainer = (Trainer) o;

                if (id != trainer.id) return false;
                if (userId != trainer.userId) return false;
                return specializationId == trainer.specializationId;
            }

            @Override
            public int hashCode() {
                int result = id;
                result = 31 * result + userId;
                result = 31 * result + specializationId;
                return result;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public void setId(int id) {
                this.id = id;
            }

            public int getSpecializationId() {
                return specializationId;
            }

            public void setSpecializationId(int specializationId) {
                this.specializationId = specializationId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        static class TrainingType implements IModel {
            private int id;
            @NotNull
            private String name;

            public TrainingType(int id, @NotNull String name) {
                this.id = id;
                this.name = name;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                TrainingType that = (TrainingType) o;

                if (id != that.id) return false;
                return name.equals(that.name);
            }

            @Override
            public int hashCode() {
                int result = id;
                result = 31 * result + name.hashCode();
                return result;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public void setId(int id) {
                this.id = id;
            }

            public @NotNull String getName() {
                return name;
            }

            public void setName(@NotNull String name) {
                this.name = name;
            }
        }

        public static class Training implements IModel {
            private int id;
            private int duration;
            @NotNull
            private String name;
            @NotNull
            private LocalDate date;
            private int traineeId;
            private int trainerId;
            private int trainingTypeId;

            public Training(int id, int traineeId, int trainerId, int trainingTypeId, @NotNull String name, @NotNull LocalDate date, int duration) {
                this.id = id;
                this.traineeId = traineeId;
                this.trainerId = trainerId;
                this.trainingTypeId = trainingTypeId;
                this.name = name;
                this.date = date;
                this.duration = duration;
            }

            public Training(int traineeId, int trainerId, int trainingTypeId, @NotNull String name, @NotNull LocalDate date, int duration) {
                this.traineeId = traineeId;
                this.trainerId = trainerId;
                this.trainingTypeId = trainingTypeId;
                this.name = name;
                this.date = date;
                this.duration = duration;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Training training = (Training) o;

                if (id != training.id) return false;
                if (duration != training.duration) return false;
                if (traineeId != training.traineeId) return false;
                if (trainerId != training.trainerId) return false;
                if (trainingTypeId != training.trainingTypeId) return false;
                if (!name.equals(training.name)) return false;
                return date.equals(training.date);
            }

            @Override
            public int hashCode() {
                int result = id;
                result = 31 * result + duration;
                result = 31 * result + name.hashCode();
                result = 31 * result + date.hashCode();
                result = 31 * result + traineeId;
                result = 31 * result + trainerId;
                result = 31 * result + trainingTypeId;
                return result;
            }

            @NotNull
            public String getName() {
                return name;
            }

            public void setName(@NotNull String name) {
                this.name = name;
            }

            @NotNull
            public LocalDate getDate() {
                return date;
            }

            public void setDate(@NotNull LocalDate date) {
                this.date = date;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public void setId(int id) {
                this.id = id;
            }

            public int getTraineeId() {
                return traineeId;
            }

            public void setTraineeId(int traineeId) {
                this.traineeId = traineeId;
            }

            public int getTrainerId() {
                return trainerId;
            }

            public void setTrainerId(int trainerId) {
                this.trainerId = trainerId;
            }

            public int getTrainingTypeId() {
                return trainingTypeId;
            }

            public void setTrainingTypeId(int trainingTypeId) {
                this.trainingTypeId = trainingTypeId;
            }
        }
    }
}
