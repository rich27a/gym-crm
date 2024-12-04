package com.example.gym.controllers;

import com.example.gym.dtos.*;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.services.TrainerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    private final TrainerMapper trainerMapper;

    public TrainerController(TrainerService trainerService, TrainerMapper trainerMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    @GetMapping
    public List<Trainer> getAll(){
        return trainerService.getTrainers();
    }
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> getTrainerByUsername(@PathVariable String username){
        return trainerService.findTrainerByUsername(username)
                .map(trainer -> ResponseEntity.ok(trainerMapper.toResponse(trainer)))
                .orElseThrow(() -> new ResourceNotFoundException("trainer not found"));
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

    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileUpdateResponseDTO> update(@PathVariable(required = true) String username, @Valid @RequestBody UpdateTrainerRequestDTO trainer){
        return trainerService.updateTrainerProfile(username, trainer)
                .map(updatedTrainer -> ResponseEntity.ok(trainerMapper.toUpdateResponse(updatedTrainer)))
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
    }


    @DeleteMapping("/{username}")
    public boolean delete(@PathVariable String username){
        return trainerService.deleteTrainerByUsername(username);
    }
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingInfoResponseDTO>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String traineeName) {

        return trainerService.getTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName)
                .map(trainingInfoResponseDTOS -> trainingInfoResponseDTOS.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(trainingInfoResponseDTOS) : ResponseEntity.ok(trainingInfoResponseDTOS))
                .orElseThrow(() -> new ResourceNotFoundException("trainer not found"));
    }
    @PutMapping("/{username}/change-password")
    public Boolean updatePassword(@PathVariable String username,
                                  @RequestBody PasswordChangeDto passwordChangeDto
    ){
        return trainerService.changePassword(username, passwordChangeDto.getNewPassword(), passwordChangeDto.getOldPassword());
    }


    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateTrainerStatus(@PathVariable String username, @Valid @RequestBody StatusUpdateDTO statusUpdateDTO){
        trainerService.activate(username, statusUpdateDTO.getActive());
        return ResponseEntity.ok().build();
    }
}
