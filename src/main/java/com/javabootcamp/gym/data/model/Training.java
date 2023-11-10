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
}