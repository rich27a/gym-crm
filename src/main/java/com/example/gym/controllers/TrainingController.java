package com.example.gym.controllers;

import com.example.gym.dtos.TrainingRequestDTO;
import com.example.gym.dtos.TrainingTypeDTO;
import com.example.gym.services.TrainingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trainings", description = "Controller for trainings")
@RestController
@RequestMapping("/api/trainings")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<?> addTraining(@Valid @RequestBody TrainingRequestDTO training){
        return trainingService.createTraining(training)
                .map(created -> ResponseEntity.ok(created))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false));
    }
    @GetMapping("/training-types")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes(){
        List<TrainingTypeDTO> trainingTypeDTOS = trainingService.getAllTrainingTypes();
        return trainingTypeDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(trainingTypeDTOS);
    }
}
