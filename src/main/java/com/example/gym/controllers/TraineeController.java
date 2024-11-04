package com.example.gym.controllers;

import com.example.gym.models.Trainee;
import com.example.gym.services.TraineeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return traineeService.getTrainees();
    }

    @GetMapping("/search/{username}")
    public Trainee getTraineeByUsername(@PathVariable("username") String username){
        System.out.println("searching trainee with username: " + username);
        return traineeService.findTraineeByUsername(username);
    }
    @DeleteMapping("/{username}")
    public void saveUpdate(@PathVariable String username){
        System.out.println("deleting trainee with username: " + username);
        traineeService.deleteTraineeByUsername(username);
    }

    @GetMapping("/{id}")
    public Optional<Trainee> getTraineeById(@PathVariable Long id){
        return traineeService.findTraineeById(id);
    }
}
