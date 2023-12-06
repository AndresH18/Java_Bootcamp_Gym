package com.javabootcamp.reportingservice.controller.health;

import com.javabootcamp.reportingservice.configuration.prometheus.ConsumerMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator")
public class HealthController {
    private final ConsumerMetrics consumerMetrics;

    @Autowired
    public HealthController(ConsumerMetrics consumerMetrics) {
        this.consumerMetrics = consumerMetrics;
    }

    @GetMapping("reporting-service/messages-received")
    public long reportsSent() {
        return consumerMetrics.messagesReceived();
    }
}
