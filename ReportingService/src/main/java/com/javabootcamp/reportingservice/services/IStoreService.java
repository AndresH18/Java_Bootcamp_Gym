package com.javabootcamp.reportingservice.services;

public interface IStoreService<T> {
    boolean store(T t);
}
