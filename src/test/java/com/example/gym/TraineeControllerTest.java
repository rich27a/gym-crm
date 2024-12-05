package com.example.gym;

import com.example.gym.controllers.TraineeController;
import com.example.gym.dtos.TraineeProfileResponse;
import com.example.gym.dtos.TraineeRegistrationRequestDTO;
import com.example.gym.dtos.TraineeRegistrationResponseDTO;
import com.example.gym.mappers.TraineeMapper;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.models.Trainee;
import com.example.gym.services.TraineeService;
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

@WebMvcTest(TraineeController.class)
@Import(TraineeControllerTest.TestSecurityConfiguration.class)
public class TraineeControllerTest {

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
    private TraineeService traineeService;

    @Test
    @DisplayName("Getting trainee by username")
    void testGetTraineeByUsername() throws Exception {

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        TraineeProfileResponse traineeProfileResponse = new TraineeProfileResponse();
        traineeProfileResponse.setFirstName("John");
        traineeProfileResponse.setLastName("Doe");


        Mockito.when(traineeService.findTraineeByUsername("John.Doe"))
                .thenReturn(Optional.of(trainee));

        Mockito.when(traineeMapper.toResponse(trainee))
                .thenReturn(traineeProfileResponse);

        mockMvc.perform(get("/api/trainees/John.Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "testPass")
    @DisplayName("Register a new trainee")
    void testRegisterTrainee() throws Exception {
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");

        TraineeRegistrationResponseDTO responseDTO = new TraineeRegistrationResponseDTO();
        responseDTO.setUsername("John.Doe");
        responseDTO.setPassword("password");


        Trainee createdTrainee = new Trainee();
        createdTrainee.setUsername("John.Doe");

        Mockito.when(traineeService.createTraineeProfile(Mockito.any(TraineeRegistrationRequestDTO.class)))
                .thenReturn(createdTrainee);

        Mockito.when(traineeMapper.toTraineeRegistrationResponse(Mockito.any(Trainee.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "firstName": "John",
                    "lastName": "Doe"
                }
                """)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("username:password".getBytes())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/trainees/John.Doe"))
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("password"));
    }

}
