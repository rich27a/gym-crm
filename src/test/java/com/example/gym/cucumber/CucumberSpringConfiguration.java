package com.example.gym.cucumber;

import com.example.gym.GymApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GymApplication.class)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {
}
