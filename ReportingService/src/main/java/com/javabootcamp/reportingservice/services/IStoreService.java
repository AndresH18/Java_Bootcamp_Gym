package com.javabootcamp.reportingservice.services;

public interface IStoreService<T> {
    boolean send(T t);
}
