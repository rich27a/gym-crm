package com.example.gym;

import com.example.gym.controllers.TrainingController;
import com.example.gym.dtos.TrainingRequestDTO;
import com.example.gym.dtos.TrainingTypeDTO;
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

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingController.class)
@Import(TrainingControllerTest.TestSecurityConfiguration.class)
@ActiveProfiles("test")
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

    @Test
    @DisplayName("Get all training Types")
    void testGetAllTrainingTypes() throws Exception {
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();
        trainingTypeDTO.setTrainingType("CARDIO");
        trainingTypeDTO.setTrainingTypeId(1L);

        TrainingTypeDTO trainingTypeDTO2 = new TrainingTypeDTO();
        trainingTypeDTO2.setTrainingType("YOGA");
        trainingTypeDTO2.setTrainingTypeId(2L);

        List<TrainingTypeDTO> trainingTypeDTOS = new ArrayList<>(Arrays.asList(trainingTypeDTO, trainingTypeDTO2));

        Mockito.when(trainingService.getAllTrainingTypes()).thenReturn(trainingTypeDTOS);

        mockMvc.perform(get("/api/trainings/training-types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].trainingTypeId").value(1))
                .andExpect(jsonPath("$[0].trainingType").value("CARDIO"))
                .andExpect(jsonPath("$[1].trainingTypeId").value(2))
                .andExpect(jsonPath("$[1].trainingType").value("YOGA"));
    }
}
