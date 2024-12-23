package com.example.gym.observability;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {

    private static final long THRESHOLD = 100 * 1024 * 1024; // 100 MB

    @Override
    public Health health() {
        File root = new File("/");
        long freeSpace = root.getFreeSpace();

        if (freeSpace > THRESHOLD) {
            return Health.up().withDetail("diskSpace", "Sufficient disk space available").build();
        } else {
            return Health.down().withDetail("diskSpace", "Low disk space. Free: " + freeSpace + " bytes").build();
        }
    }
}
