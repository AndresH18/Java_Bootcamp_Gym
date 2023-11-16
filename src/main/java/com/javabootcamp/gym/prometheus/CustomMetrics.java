package com.javabootcamp.gym.prometheus;

import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {
    private final Counter customCounter;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.customCounter = Counter.builder("custom_counter")
                .description("A custom counter metric")
                .register(meterRegistry);
    }

    public void increment() {
        customCounter.increment();
    }

    public double getCount() {
        return customCounter.count();
    }
}
