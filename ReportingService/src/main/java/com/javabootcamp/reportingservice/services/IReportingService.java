package com.javabootcamp.reportingservice.services;

public interface IReportingService<T, R> {

    void create(T t);

    void delete(T t);
}
