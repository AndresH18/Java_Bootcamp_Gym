package com.javabootcamp.gym.data.model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "Trainings", schema = "dbo")
public class Training implements IModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "training_duration")
    private int duration;
    @NotNull
    @Column(name = "training_name")
    private String name;
    @NotNull
    @Column(name = "training_date")
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

    public Training(Trainer trainer, Trainee trainee, TrainingType trainingType, @NotNull String name, int duration, @NotNull LocalDate date) {
        this.duration = duration;
        this.name = name;
        this.date = date;
        this.trainingType = trainingType;
        this.trainee = trainee;
        this.trainer = trainer;
    }

    public Training() {
    }

    // TODO: equals(), hashcode()

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

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
}