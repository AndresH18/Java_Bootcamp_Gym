package com.javabootcamp.gym.data.model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "Trainings")
public class Training implements IModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int duration;
    @NotNull
    private String name;
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Deprecated
    private int traineeId;
    @Deprecated
    private int trainerId;
    @Deprecated
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

    public Training() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Training training = (Training) o;

        if (id != training.id) return false;
        if (traineeId != training.traineeId) return false;
        if (trainerId != training.trainerId) return false;
        if (trainingTypeId != training.trainingTypeId) return false;
        if (duration != training.duration) return false;
        if (!name.equals(training.name)) return false;
        return date.equals(training.date);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + traineeId;
        result = 31 * result + trainerId;
        result = 31 * result + trainingTypeId;
        result = 31 * result + name.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + duration;
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

    @Deprecated
    public int getTraineeId() {
        return traineeId;
    }

    @Deprecated
    public void setTraineeId(int traineeId) {
        this.traineeId = traineeId;
    }

    @Deprecated
    public int getTrainerId() {
        return trainerId;
    }

    @Deprecated
    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    @Deprecated
    public int getTrainingTypeId() {
        return trainingTypeId;
    }

    @Deprecated
    public void setTrainingTypeId(int trainingTypeId) {
        this.trainingTypeId = trainingTypeId;
    }


}