package com.javabootcamp.gym.data.model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Training implements IModel {
    private int id;
    private int traineeId;
    private int trainerId;
    private int trainingTypeId;
    @NotNull
    private String name;
    @NotNull
    private Date date;
    private int duration;

    public Training(int id, int traineeId, int trainerId, int trainingTypeId, @NotNull String name, @NotNull Date date, int duration) {
        this.id = id;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingTypeId = trainingTypeId;
        this.name = name;
        this.date = date;
        this.duration = duration;
    }

    public Training(int traineeId, int trainerId, int trainingTypeId, @NotNull String name, @NotNull Date date, int duration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingTypeId = trainingTypeId;
        this.name = name;
        this.date = date;
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

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NotNull Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}