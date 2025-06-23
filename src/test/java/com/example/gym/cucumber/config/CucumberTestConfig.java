package com.example.gym.cucumber.config;

import com.example.gym.services.TraineeService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class CucumberTestConfig {

    @Bean
    @Primary
    public TraineeService traineeService() {
        return Mockito.mock(TraineeService.class);
    }
}

