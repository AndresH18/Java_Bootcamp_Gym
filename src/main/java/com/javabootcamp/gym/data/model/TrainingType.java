package com.javabootcamp.gym.data.model;

import org.jetbrains.annotations.NotNull;

public class TrainingType implements IModel {
    private int id;
    @NotNull
    private String name;

    public TrainingType(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public TrainingType(@NotNull String name) {
        this.name = name;
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
        return id;
    }

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
