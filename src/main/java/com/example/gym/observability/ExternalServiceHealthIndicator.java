package com.example.gym.observability;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {
    private static final String EXTERNAL_SERVICE_URL = "https://catfact.ninja/fact";
    @Override
    public Health health() {
        try {
            URL url = new URL(EXTERNAL_SERVICE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                return Health.up().withDetail("externalService", "Service is reachable").build();
            } else {
                return Health.down().withDetail("externalService", "Service returned status: " + responseCode).build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("externalService", "Service is unreachable").withException(e).build();
        }
    }

}
