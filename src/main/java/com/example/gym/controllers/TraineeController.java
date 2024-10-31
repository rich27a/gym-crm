package com.example.gym.controllers;

import com.example.gym.models.Trainee;
import com.example.gym.services.TraineeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public Trainee saveTrainee(@RequestBody Trainee trainee){
        return traineeService.createTraineeProfile(trainee);
    }
    @GetMapping
    public List<Trainee> getAllTrainees(){
        return new ArrayList<>();
    }
}
