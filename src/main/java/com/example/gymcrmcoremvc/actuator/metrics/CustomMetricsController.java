package com.example.gymcrmcoremvc.actuator.metrics;

import io.micrometer.core.instrument.Counter;
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

//    @Autowired
//    private CustomMetricsCollector customMetricsCollector;


    private final Counter customCounter;

    //private final Gauge gauge;


    @Autowired
    public CustomMetricsController(MeterRegistry meterRegistry) {
        this.customCounter = Counter.builder("custom_counter")
                .description("A custom counter metric")
                .register(meterRegistry);
    //    this.gauge = gauge;
    }

    public void incrementCustomCounter() {
        customCounter.increment();

    }
    @GetMapping("/hello")
    public String hello() {
        // Record a counter metric for the number of requests
        meterRegistry.counter("http.requests.total", "uri", "/hello").increment();

        return "Hello, World!";
    }

    @GetMapping("/hit-endpoint")
    public String hitEndpoint() {
        // Increment the custom counter metric
        //customMetricsCollector.incrementCustomCounter();
        customCounter.increment();
    //    System.out.println(gauge.value());
        return "Endpoint hit!";
    }
}
