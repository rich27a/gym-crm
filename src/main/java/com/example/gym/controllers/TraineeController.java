package com.example.gym.controllers;

import com.example.gym.dtos.PasswordChangeDto;
import com.example.gym.dtos.TraineeProfileResponse;
import com.example.gym.mappers.TraineeMapper;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
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
    public Optional<Trainee> update(@Valid @RequestBody Trainee trainee){
        return traineeService.updateTraineeProfile(trainee);
    }
    @GetMapping
    public List<Trainee> getAll(){
        return traineeService.getTrainees();
    }



    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> getByUsername(@PathVariable String username){
        return traineeService.findTraineeByUsername(username)
                .map(trainee -> ResponseEntity.ok(TraineeMapper.INSTANCE.toResponse(trainee)))
                .orElseThrow(() -> new EntityNotFoundException("trainee not found"));
//        return traineeService.findTraineeByUsername(username)
//                .map(trainee -> {
//                    TraineeProfileResponse response = new TraineeProfileResponse(
//                            trainee.getFirstName(),
//                            trainee.getLastName(),
//                            LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(trainee.getDateOfBirth()) ),
//                            trainee.getAddress(),
//                            trainee.isActive()
//                    );
//                    List<TraineeProfileResponse.TrainerInfo> trainerInfoList = trainee.getTrainerList().stream()
//                            .map(trainer -> new TraineeProfileResponse.TrainerInfo(
//                                    trainer.getUsername(),
//                                    trainer.getFirstName(),
//                                    trainer.getLastName(),
//                                    trainer.getTrainingType().toString()
//                            )).toList();
//                    response.setTrainers(trainerInfoList);
//                    return ResponseEntity.ok(response);
//                }).orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
    }

    @DeleteMapping("/{username}")
    public boolean delete(@PathVariable String username){
        return traineeService.deleteTraineeByUsername(username);
    }

    @GetMapping("/{username}/trainings")
    public List<Training> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) Specialization trainingType) {

        return traineeService.getTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName, trainingType);
    }

    @PutMapping("/{traineeId}/trainers")
    public Trainee updateTraineeTrainers(
            @PathVariable Long traineeId,
            @RequestBody List<Long> trainerIds) {
        return traineeService.updateTraineeTrainerList(traineeId, trainerIds);
    }
    @PutMapping("/{username}/change-password")
    public Boolean updatePassword(@PathVariable String username,
                                  @RequestBody PasswordChangeDto passwordChangeDto
                                  ){
        return traineeService.changePassword(username, passwordChangeDto.getNewPassword(), passwordChangeDto.getOldPassword());
    }


    @GetMapping("/{username}/unassigned-trainers")
    public List<Trainer> getUnassignedTrainers(@PathVariable String username) {
        return traineeService.getUnassignedTrainers(username);
    }
    @PutMapping("/{id}/activate")
    public void activateDeactivate(@PathVariable Long id){
        traineeService.activate(id);
    }
}
