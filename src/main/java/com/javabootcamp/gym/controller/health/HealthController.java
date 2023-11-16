package com.javabootcamp.gym.controller.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator")
public class HealthController {
    private final HealthIndicator databaseHealthIndicator;

    @Autowired
    public HealthController(HealthIndicator databaseHealthIndicator) {
        this.databaseHealthIndicator = databaseHealthIndicator;
    }

    @GetMapping("db-health")
    public Health customDbHealth() {
        return databaseHealthIndicator.health();
    }
}
