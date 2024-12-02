package com.example.gym.controllers;

import com.example.gym.dtos.*;
import com.example.gym.mappers.TraineeMapper;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.models.*;
import com.example.gym.services.TraineeService;
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
    public ResponseEntity<Map<String, String>> save(@Valid @RequestBody Trainee trainee){
        Trainee traineeCreated = traineeService.createTraineeProfile(trainee);
        URI location = URI.create("/api/trainees/" + traineeCreated.getId());
        Map<String, String> response = new HashMap<>();
        response.put("username", traineeCreated.getUsername());
        response.put("password", traineeCreated.getPassword());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping
    public ResponseEntity<TraineeProfileUpdateResponseDTO> update(@Valid @RequestBody Trainee trainee){
        return traineeService.updateTraineeProfile(trainee)
                .map(traineeUpdated -> ResponseEntity.ok(traineeMapper.toUpdatedResponse(traineeUpdated)))
                .orElseThrow(()-> new EntityNotFoundException("trainee not found"));
    }
    @GetMapping
    public List<Trainee> getAll(){
        return traineeService.getTrainees();
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
                .map(trainerInfoResponseDTOS -> ResponseEntity.ok(trainerInfoResponseDTOS))
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
                .map(trainer -> trainerMapper.toTrainerInfoResponse(trainer))
                .toList();
        if (unassignedTrainers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(unassignedTrainers);
    }
    @PutMapping("/{id}/activate")
    public void activateDeactivate(@PathVariable Long id){
        traineeService.activate(id);
    }
}
