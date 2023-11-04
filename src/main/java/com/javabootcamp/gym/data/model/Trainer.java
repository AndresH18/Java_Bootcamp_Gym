package com.javabootcamp.gym.data.model;

public class Trainer implements IModel {
    private int id;
    private int userId;

    // Training type id
    private int specialization;

    public Trainer(int id, int userId, int specialization) {
        this.id = id;
        this.userId = userId;
        this.specialization = specialization;
    }

    public Trainer(int userId, int specialization) {
        this.userId = userId;
        this.specialization = specialization;
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

    public int getSpecialization() {
        return specialization;
    }

    public void setSpecialization(int specialization) {
        this.specialization = specialization;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
