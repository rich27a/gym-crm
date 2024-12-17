package com.example.gym;

import com.example.gym.controllers.TrainerController;
import com.example.gym.dtos.*;
import com.example.gym.mappers.TraineeMapper;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.TrainingType;
import com.example.gym.services.TraineeService;
import com.example.gym.services.TrainerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
@Import(TrainerControllerTest.TestSecurityConfiguration.class)
@ActiveProfiles("test")
public class TrainerControllerTest {
    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .csrf(csrf -> csrf.disable())
                    .build();
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TraineeMapper traineeMapper;
    @MockBean
    private TrainerMapper trainerMapper;
    @MockBean
    private TrainerService trainerService;

    @Test
    @DisplayName("Getting trainer by username")
    void testGetTraineeByUsername() throws Exception {

        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setTrainingType(TrainingType.fromString("YOGA"));


        TrainerProfileResponse trainerProfileResponse = new TrainerProfileResponse();
        trainerProfileResponse.setFirstName("John");
        trainerProfileResponse.setLastName("Doe");


        Mockito.when(trainerService.findTrainerByUsername("John.Doe"))
                .thenReturn(Optional.of(trainer));

        Mockito.when(trainerMapper.toResponse(trainer))
                        .thenReturn(trainerProfileResponse);

        mockMvc.perform(get("/api/trainers/John.Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "testPass")
    @DisplayName("Register a new trainee")
    void testRegisterTrainee() throws Exception {
        TrainerRegistrationRequestDTO trainerRegistrationRequestDTO = new TrainerRegistrationRequestDTO();
        trainerRegistrationRequestDTO.setFirstName("John");
        trainerRegistrationRequestDTO.setLastName("Doe");
        trainerRegistrationRequestDTO.setSpecialization("YOGA");


        TrainerRegistrationResponseDTO trainerRegistrationResponseDTO = new TrainerRegistrationResponseDTO();
        trainerRegistrationResponseDTO.setUsername("John.Doe");
        trainerRegistrationResponseDTO.setPassword("password");

        Trainer trainer = new Trainer();
        trainer.setUsername("John.Doe");

        Mockito.when(trainerService.createTrainerProfile(Mockito.any(TrainerRegistrationRequestDTO.class)))
                .thenReturn(trainerRegistrationResponseDTO);

        Mockito.when(trainerMapper.toRegistrationResponse(Mockito.any(Trainer.class)))
                .thenReturn(trainerRegistrationResponseDTO);

        mockMvc.perform(post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "specialization":"YOGA"
                }
                """)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("username:password".getBytes())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/trainers/John.Doe"))
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("password"));
    }


}
