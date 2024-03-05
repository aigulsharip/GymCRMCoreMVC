package com.example.gymcrmcoremvc.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    // #1. Custom health indicator implementation: randomly selecting health status

    @Override
    public Health health() {
        double chance = ThreadLocalRandom.current().nextDouble();
        Health.Builder status = Health.up();
        if (chance > 0.5) {
            status = Health.down();
        }

        Map<String, Object> details = new HashMap<>();
        details.put("chance", chance);
        details.put("strategy", "thread-local");
        String statusString = chance < 0.5 ? "Application is healthy": "Application is not healthy";
        details.put("status", statusString);

        return status.withDetails(details).build();
    }



    /*
    // #2. Custom health indicator implementation: manually select health status
    public Health health() {
        // Logic to check the health of your application
        boolean isHealthy = true; // Example health check
        if (isHealthy) {
            return Health.up().withDetail("success", "Application is healthy").build(); // Application is healthy
        } else {
            return Health.down().withDetail("error", "Application is not healthy").build(); // Application is not healthy
        }
    }
  */

    /*
    // #3. Custom health indicator implementation: with random status codes
    //Link to article: https://dev.to/manojshr/customise-health-endpoint-2e1

    private final Random randomizer = new Random();
    private final List<Integer> statusCodes = List.of(200, 204, 401, 404, 503);

    @Override
    public Health health() {
        int randomStatusCode = statusCodes.get(randomizer.nextInt(statusCodes.size()));
        Health.Builder healthBuilder = new Health.Builder();
        return (switch(randomStatusCode) {
            case 200, 204 -> healthBuilder.up()
                    .withDetail("External_Service", "Service is Up and Running ✅")
                    .withDetail("url", "https://example.com");
            case 503 -> healthBuilder.down()
                    .withDetail("External_Service", "Service is Down 🔻")
                    .withDetail("alternative_url", "https://alt-example.com");
            default -> healthBuilder.unknown().withException(new RuntimeException("Received status: " + randomStatusCode));
        }).build();
    }
     */
}