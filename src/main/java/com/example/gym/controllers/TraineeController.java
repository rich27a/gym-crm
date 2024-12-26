package com.example.gym.controllers;

import com.example.gym.dtos.*;
import com.example.gym.mappers.TraineeMapper;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.models.*;
import com.example.gym.services.TraineeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

@Tag(name = "Trainees", description = "Controller for trainees")
@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;

    public TraineeController(TraineeService traineeService, TraineeMapper traineeMapper, TrainerMapper trainerMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
    }

    @PostMapping
    public ResponseEntity<UserRegistrationResponseDTO> save(@Valid @RequestBody TraineeRegistrationRequestDTO traineeRegistrationRequestDTO){
        UserRegistrationResponseDTO traineeCreated = traineeService.createTraineeProfile(traineeRegistrationRequestDTO);
        URI location = URI.create("/api/trainees/" + traineeCreated.getUsername());
        return ResponseEntity.created(location).body(traineeCreated);
    }

    @PutMapping
    public ResponseEntity<TraineeProfileUpdateResponseDTO> update(@Valid @RequestBody Trainee trainee){
        return traineeService.updateTraineeProfile(trainee)
                .map(traineeUpdated -> ResponseEntity.ok(traineeMapper.toUpdatedResponse(traineeUpdated)))
                .orElseThrow(()-> new EntityNotFoundException("trainee not found"));
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> getByUsername(@PathVariable String username){
        return traineeService.findTraineeByUsername(username)
                .map(trainee -> ResponseEntity.ok(traineeMapper.toResponse(trainee)))
                .orElseThrow(() -> new EntityNotFoundException("trainee not found"));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> delete(@PathVariable(required = true) String username){
        return traineeService.deleteTraineeByUsername(username) ? ResponseEntity.ok("trainee deleted successfully") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("trainee not found");
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingInfoResponseDTO>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) Specialization trainingType) {

        return traineeService.getTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName, trainingType)
                .map(trainings -> {
                    return trainings.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(trainings) : ResponseEntity.ok(trainings);
                })
                .orElseThrow(() -> new ResourceNotFoundException("trainee not found"));
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerInfoResponseDTO>> updateTraineeTrainers(
            @PathVariable String username,
            @RequestBody(required = true) List<String> trainersUsernames) {
        return traineeService.updateTraineeTrainerList(username, trainersUsernames)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("trainee not found"));
    }
    @PutMapping("/{username}/change-password")
    public Boolean updatePassword(@PathVariable String username,
                                  @RequestBody PasswordChangeDto passwordChangeDto
                                  ){
        return traineeService.changePassword(username, passwordChangeDto.getNewPassword(), passwordChangeDto.getOldPassword());
    }


    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerInfoResponseDTO>> getUnassignedTrainers(@PathVariable String username) {
        List<TrainerInfoResponseDTO> unassignedTrainers = traineeService.getUnassignedTrainers(username).stream()
                .map(trainerMapper::toTrainerInfoResponse)
                .toList();
        return unassignedTrainers.isEmpty()
                ?  ResponseEntity.noContent().build()
                : ResponseEntity.ok(unassignedTrainers);
    }
    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateTraineeStatus(@PathVariable String username, @RequestBody StatusUpdateDTO statusUpdateDTO){
        System.out.println("statusUpdateDTO = " + statusUpdateDTO.getActive());
        traineeService.activate(username, statusUpdateDTO.getActive());
        return ResponseEntity.ok().build();
    }
}
