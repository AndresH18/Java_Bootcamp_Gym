package com.javabootcamp.gym.data.model;

public interface ICopy<T> {
    T copyFrom(T t);
}
