package com.example.gym.cucumber.stepdefinitions;

import com.example.gym.dtos.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.gym.models.Trainee;
import com.example.gym.services.TraineeService;
import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.*;

public class TraineeStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TraineeService traineeService;

    private ResponseEntity<?> response;
    private Trainee testTrainee;

    @Given("the system has a test trainee with username {string}")
    public void theSystemHasATestTraineeWithUsername(String username) {
        testTrainee = new Trainee();
        testTrainee.setFirstName("Test");
        testTrainee.setLastName("Trainee");
        LocalDate localDate = LocalDate.of(1990, 1, 1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay(defaultZoneId).toInstant();
        Date date = Date.from(instant);
        testTrainee.setDateOfBirth(date);
        testTrainee.setAddress("Test Address");
        testTrainee.setActive(true);

        Mockito.when(traineeService.findTraineeByUsername(username))
                .thenReturn(Optional.of(testTrainee));
    }

    @When("I request the trainee profile with username {string}")
    public void iRequestTheTraineeProfileWithUsername(String username) {
        response = restTemplate.getForEntity("/api/trainees/" + username, Trainee.class);
    }

    @When("I create a new trainee with the following details:")
    public void iCreateANewTraineeWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName(data.get("firstName"));
        newTrainee.setLastName(data.get("lastName"));

        UserRegistrationResponseDTO credentials = new UserRegistrationResponseDTO();
        credentials.setUsername("john.doe");
        credentials.setPassword("password123");

        Mockito.when(traineeService.createTraineeProfile(Mockito.any(TraineeRegistrationRequestDTO.class)))
                .thenReturn(credentials);

        response = restTemplate.postForEntity("/api/trainees", newTrainee, UserRegistrationResponseDTO.class);
    }

    @When("I update the trainee with username {string} with the following details:")
    public void iUpdateTheTraineeWithUsernameWithTheFollowingDetails(String username, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName(data.get("firstName"));
        newTrainee.setLastName(data.get("lastName"));

        TraineeProfileUpdateResponseDTO updatedTrainee = new TraineeProfileUpdateResponseDTO();
        updatedTrainee.setFirstName(data.get("firstName"));
        updatedTrainee.setLastName(data.get("lastName"));
        updatedTrainee.setDateOfBirth(LocalDate.parse(data.get("dateOfBirth")));
        updatedTrainee.setAddress(data.get("address"));
        updatedTrainee.setActive(Boolean.parseBoolean(data.get("active")));

        Mockito.when(traineeService.updateTraineeProfile(Mockito.any(Trainee.class))).thenReturn(Optional.of(newTrainee));

        response = restTemplate.exchange(
                "/api/trainees/" + username,
                HttpMethod.PUT,
                new HttpEntity<>(updatedTrainee),
                TraineeProfileUpdateResponseDTO.class);
    }

    @When("I delete the trainee with username {string}")
    public void iDeleteTheTraineeWithUsername(String username) {
        response = restTemplate.exchange(
                "/api/trainees/" + username,
                HttpMethod.DELETE,
                null,
                Void.class);
    }

    @Given("the trainee {string} has training sessions")
    public void theTraineeHasTrainingSessions(String username) {
        // Mock setup for trainee having training sessions
    }

    @When("I request the trainings for trainee {string}")
    public void iRequestTheTrainingsForTrainee(String username) {
        response = restTemplate.getForEntity("/api/trainees/" + username + "/trainings", Object.class);
    }

    @When("I update the status of trainee {string} to inactive")
    public void iUpdateTheStatusOfTraineeToInactive(String username) {
        response = restTemplate.exchange(
                "/api/trainees/" + username + "/status",
                HttpMethod.PATCH,
                new HttpEntity<>(Map.of("active", false)),
                Void.class);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), response.getStatusCode());
    }

    @Then("the response should contain the trainee's details")
    public void theResponseShouldContainTheTraineeSDetails() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof TraineeProfileUpdateResponseDTO);
    }

    @Then("the response should contain the generated username and password")
    public void theResponseShouldContainTheGeneratedUsernameAndPassword() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof TraineeRegistrationResponseDTO);
        TraineeRegistrationResponseDTO credentials = (TraineeRegistrationResponseDTO) response.getBody();
        assertNotNull(credentials.getUsername());
        assertNotNull(credentials.getPassword());
    }

    @Then("the response should contain the updated trainee details")
    public void theResponseShouldContainTheUpdatedTraineeDetails() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof TraineeProfileUpdateResponseDTO);
    }

    @Then("the trainee should be removed from the system")
    public void theTraineeShouldBeRemovedFromTheSystem() {
        Mockito.verify(traineeService).deleteTraineeByUsername(Mockito.anyString());
    }

    @Then("the response should contain the trainee's training sessions")
    public void theResponseShouldContainTheTraineeSTrainingSessions() {
        assertNotNull(response.getBody());
    }

    @Then("the trainee's status should be updated to inactive")
    public void theTraineeSStatusShouldBeUpdatedToInactive() {
        Mockito.verify(traineeService).activate(Mockito.anyString(), Mockito.eq(false));
    }
}
