package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.actuator.CustomMetricsCollector;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class CustomMetricsController {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private CustomMetricsCollector customMetricsCollector;

    @GetMapping("/hello")
    public String hello() {
        // Record a counter metric for the number of requests
        meterRegistry.counter("http.requests.total", "uri", "/hello").increment();

        return "Hello, World!";
    }

    @GetMapping("/hit-endpoint")
    public String hitEndpoint() {
        // Increment the custom counter metric
        customMetricsCollector.incrementCustomCounter();
        return "Endpoint hit!";
    }
}
