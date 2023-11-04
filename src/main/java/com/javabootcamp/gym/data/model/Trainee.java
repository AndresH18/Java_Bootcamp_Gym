package com.javabootcamp.gym.data.model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Trainee implements IModel {
    private int id;
    private int userId;
    @NotNull
    private Date dateOfBirth;
    private String address;

    public Trainee(int id, int userId, @NotNull Date dateOfBirth, String address) {
        this.id = id;
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(int userId, @NotNull Date dateOfBirth, String address) {
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trainee trainee = (Trainee) o;

        return id == trainee.id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public @NotNull Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
