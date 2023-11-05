package com.javabootcamp.gym.data.model;

public class Trainer implements IModel {
    private int id;
    private int userId;

    // Training type id
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

        return id == trainer.id;
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
