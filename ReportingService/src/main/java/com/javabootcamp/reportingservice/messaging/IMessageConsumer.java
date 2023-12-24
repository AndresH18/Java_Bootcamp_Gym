package com.javabootcamp.reportingservice.messaging;

public interface IMessageConsumer<T> {
    void consumeMessage(T t);
}
