package com.example.gym.controllers;

import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.services.TrainerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping
    public List<Trainer> getAll(){
        return trainerService.getTrainers();
    }
    @GetMapping("/search/{username}")
    public Optional<Trainer> getTrainerByUsername(@PathVariable String username){
        return trainerService.findTrainerByUsername(username);
    }

    @PostMapping
    public Trainer createTrainer(@RequestBody Trainer trainer){
        return trainerService.createTrainerProfile(trainer);
    }

    @PutMapping
    public Optional<Trainer> update(@RequestBody Trainer trainer){
        return trainerService.updateTrainerProfile(trainer);
    }


    @DeleteMapping("/{username}")
    public boolean delete(@PathVariable String username){
        return trainerService.deleteTrainerByUsername(username);
    }

}
