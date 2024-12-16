package com.example.gym.observability;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class CustomMetricsService {

    private final MeterRegistry meterRegistry;

    public CustomMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void trackCustomMetric() {
        meterRegistry.counter("custom_metric.counter", "tag", "example").increment();
    }

    public void trackTimeTaken(Runnable task) {
        meterRegistry.timer("custom_metric.timer", "tag", "example").record(task);
    }
}
