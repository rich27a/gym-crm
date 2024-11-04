package com.example.gym.controllers;

import com.example.gym.models.Trainer;
import com.example.gym.services.TrainerService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/{username}")
    public Optional<Trainer> getTrainerByName(@PathVariable String username){
        return trainerService.findTrainerByUsername(username);
    }

    @PostMapping
    public Trainer createTrainer(@RequestBody Trainer trainer){
        return trainerService.createTrainerProfile(trainer);
    }

}
