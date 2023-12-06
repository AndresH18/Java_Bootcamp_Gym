package com.javabootcamp.reportingservice.configuration.prometheus;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class ConsumerMetrics {
    private final AtomicLong messagesReceived = new AtomicLong(0);

    public void incrementMessagesReceived() {
        messagesReceived.incrementAndGet();
    }

    public long messagesReceived() {
        return messagesReceived.get();
    }

}
