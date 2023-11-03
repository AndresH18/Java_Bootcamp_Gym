package com.javabootcamp.gym.data.model;

public interface IModel {
    int getId();

    void setId(int id);

    boolean equals(Object o);

    int hashCode();
}
