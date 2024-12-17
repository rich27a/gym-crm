package com.example.gym;

import com.example.gym.controllers.TrainingController;
import com.example.gym.dtos.TrainingRequestDTO;
import com.example.gym.repositories.TrainingRepository;
import com.example.gym.services.TrainingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
@Import(TrainingControllerTest.TestSecurityConfiguration.class)
public class TrainingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrainingRepository trainingRepository;
    @MockBean
    private TrainingService trainingService;

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .csrf(csrf -> csrf.disable())
                    .build();
        }
    }

    @Test
    @DisplayName("Add new training successfully")
    void testAddNewTraining() throws Exception {
        TrainingRequestDTO trainingRequestDTO = new TrainingRequestDTO();
        trainingRequestDTO.setTrainingName("Cardio Training");
        trainingRequestDTO.setTrainingDate(LocalDate.parse("2022-02-02"));
        trainingRequestDTO.setTrainingDuration(60);
        trainingRequestDTO.setTraineeUsername("Trainee");
        trainingRequestDTO.setTrainerUsername("Trainer");

        Mockito.when(trainingService.createTraining(Mockito.any(TrainingRequestDTO.class))).thenReturn(Optional.of(true));

        mockMvc.perform(post("/api/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"trainingName\": \"Cardio Training\",\n" +
                                "  \"trainingDate\": \"2022-02-02\",\n" +
                                "  \"trainingDuration\": 60,\n" +
                                "  \"traineeUsername\": \"Trainee\",\n" +
                                "  \"trainerUsername\": \"Trainer\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }



}
