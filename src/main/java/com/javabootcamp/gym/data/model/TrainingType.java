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
