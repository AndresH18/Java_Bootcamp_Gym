package com.javabootcamp.gym.configuration.prometheus;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReportingServiceMetrics {
    private final AtomicLong messagesSent = new AtomicLong(0);

    public void incrementMessagesSent() {
        messagesSent.incrementAndGet();
    }

    public long messagesSent() {
        return messagesSent.get();
    }

}
