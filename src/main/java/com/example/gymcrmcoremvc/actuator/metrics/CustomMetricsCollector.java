package com.example.gymcrmcoremvc.actuator.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomMetricsCollector {

    private final Counter customCounter;


    @Autowired
    public CustomMetricsCollector(MeterRegistry meterRegistry) {
        this.customCounter = Counter.builder("custom_counter")
                .description("A custom counter metric")
                .register(meterRegistry);
    }

    public void incrementCustomCounter() {
        customCounter.increment();

    }
}