package com.example.gym.controllers;

import com.example.gym.dtos.TrainingRequestDTO;
import com.example.gym.services.TrainingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
