package com.javabootcamp.gym.messaging.report;

public interface IReportingService<T> {
    boolean sendMessage(T t);
    boolean sendMessageAsync(T t);
}
