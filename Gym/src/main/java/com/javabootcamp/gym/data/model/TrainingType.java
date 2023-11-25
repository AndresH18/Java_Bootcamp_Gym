package com.javabootcamp.gym.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TrainingTypes", schema = "dbo")
public class TrainingType implements IModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "specialization")
    @JsonBackReference
    private List<Trainer> trainers;

    @OneToMany(mappedBy = "trainingType")
    @JsonIgnore
    private Set<Training> trainings;

    public TrainingType(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public TrainingType(@NotNull String name) {
        this.name = name;
    }

    public TrainingType() {
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

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    public Set<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(Set<Training> trainings) {
        this.trainings = trainings;
    }
}