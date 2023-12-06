package com.javabootcamp.gym.controller.health;

import com.javabootcamp.gym.configuration.prometheus.ReportingServiceMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator")
public class HealthController {
    private final HealthIndicator databaseHealthIndicator;
    private final ReportingServiceMetrics reportingServiceMetrics;

    @Autowired
    public HealthController(HealthIndicator databaseHealthIndicator, ReportingServiceMetrics reportingServiceMetrics) {
        this.databaseHealthIndicator = databaseHealthIndicator;
        this.reportingServiceMetrics = reportingServiceMetrics;
    }

    @GetMapping("db-health")
    public Health customDbHealth() {
        return databaseHealthIndicator.health();
    }

    @GetMapping("reporting-service/messages-sent")
    public long reportsSent() {
        return reportingServiceMetrics.messagesSent();
    }
}
