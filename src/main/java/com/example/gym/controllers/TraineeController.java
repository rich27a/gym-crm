package com.example.gym.controllers;

import com.example.gym.models.Trainee;
import com.example.gym.services.TraineeService;
import org.springframework.web.bind.annotation.*;

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
    public Trainee save(@RequestBody Trainee trainee){
        return traineeService.createTraineeProfile(trainee);
    }

    @PutMapping
    public Optional<Trainee> update(@RequestBody Trainee trainee){
        return traineeService.updateTraineeProfile(trainee);
    }
    @GetMapping
    public List<Trainee> getAll(){
        return traineeService.getTrainees();
    }

    @GetMapping("/search/{username}")
    public Optional<Trainee> getByUsername(@PathVariable String username){
        return traineeService.findTraineeByUsername(username);
    }
    @DeleteMapping("/{username}")
    public boolean delete(@PathVariable String username){
        return traineeService.deleteTraineeByUsername(username);
    }

    @GetMapping("/{id}")
    public Optional<Trainee> getById(@PathVariable Long id){
        return traineeService.findTraineeById(id);
    }
}
