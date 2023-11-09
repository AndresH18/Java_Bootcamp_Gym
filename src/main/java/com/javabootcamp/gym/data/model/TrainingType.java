package com.javabootcamp.gym.data.model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Entity
@Table(name = "TrainingTypes")
public class TrainingType implements IModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "specialization")
    private Set<Trainer> trainers;

    @OneToMany(mappedBy = "trainingType")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainingType that = (TrainingType) o;

        return id == that.id;
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
