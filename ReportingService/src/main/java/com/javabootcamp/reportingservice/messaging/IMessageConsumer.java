package com.javabootcamp.reportingservice.messaging;

import jakarta.jms.JMSException;

public interface IMessageConsumer<T> {
    void consumeMessage(T t) throws JMSException;
}
