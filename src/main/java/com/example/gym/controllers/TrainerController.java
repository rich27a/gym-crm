package com.example.gym.controllers;

import com.example.gym.dtos.PasswordChangeDto;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.services.TrainerService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Map<String, String>> createTrainer(@Valid @RequestBody Trainer trainer){

        Trainer createdTrainer = trainerService.createTrainerProfile(trainer);
        URI location = URI.create("/api/trainers/" + createdTrainer.getId());
        Map<String, String> response = new HashMap<>();
        response.put("username", createdTrainer.getUsername());
        response.put("password", createdTrainer.getPassword());
        
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping
    public Optional<Trainer> update(@Valid @RequestBody Trainer trainer){
        return trainerService.updateTrainerProfile(trainer);
    }


    @DeleteMapping("/{username}")
    public boolean delete(@PathVariable String username){
        return trainerService.deleteTrainerByUsername(username);
    }
    @GetMapping("/{username}/trainings")
    public List<Training> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String traineeName) {

        return trainerService.getTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName);
    }
    @PutMapping("/{username}/change-password")
    public Boolean updatePassword(@PathVariable String username,
                                  @RequestBody PasswordChangeDto passwordChangeDto
    ){
        return trainerService.changePassword(username, passwordChangeDto.getNewPassword(), passwordChangeDto.getOldPassword());
    }
    @PutMapping("/{id}/activate")
    public void activateDeactivate(@PathVariable Long id){
        trainerService.activate(id);
    }
}
