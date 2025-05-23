package com.example.gym.controllers;

import com.example.gym.dtos.*;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Trainers", description = "Controller for trainers")
@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    public TrainerController(TrainerService trainerService, TrainerMapper trainerMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    @Operation(summary = "Get all trainers", description = "Retrieves a list of all trainers in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all trainers",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Trainer.class)))
    @GetMapping
    public List<Trainer> getAll(){
        return trainerService.getTrainers();
    }

    @Operation(summary = "Get trainer by username", description = "Retrieves a specific trainer's profile using their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainer profile",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> getTrainerByUsername(@PathVariable String username){
        return trainerService.findTrainerByUsername(username)
                .map(trainer -> ResponseEntity.ok(trainerMapper.toResponse(trainer)))
                .orElseThrow(() -> new ResourceNotFoundException("trainer not found"));
    }

    @Operation(summary = "Create new trainer", description = "Creates a new trainer profile in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRegistrationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<UserRegistrationResponseDTO> createTrainer(@Valid @RequestBody TrainerRegistrationRequestDTO trainer){
        UserRegistrationResponseDTO trainerRegistrationResponseDTO = trainerService.createTrainerProfile(trainer);
        return ResponseEntity.created(URI.create("/api/trainers/"+trainerRegistrationResponseDTO.getUsername())).body(trainerRegistrationResponseDTO);
    }

    @Operation(summary = "Update trainer profile", description = "Updates an existing trainer's profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerProfileUpdateResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileUpdateResponseDTO> update(@PathVariable(required = true) String username, @Valid @RequestBody UpdateTrainerRequestDTO trainer){
        return trainerService.updateTrainerProfile(username, trainer)
                .map(updatedTrainer -> ResponseEntity.ok(trainerMapper.toUpdateResponse(updatedTrainer)))
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
    }


    @Operation(summary = "Delete trainer", description = "Deletes a trainer profile from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @DeleteMapping("/{username}")
    public boolean delete(@PathVariable String username){
        return trainerService.deleteTrainerByUsername(username);
    }

    @Operation(summary = "Get trainer's trainings", description = "Retrieves a list of trainings for a specific trainer with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainer's trainings",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingInfoResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No trainings found"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
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
    @Operation(summary = "Change trainer password", description = "Updates the password for a specific trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid password or old password incorrect"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @PutMapping("/{username}/change-password")
    public Boolean updatePassword(@PathVariable String username,
                                  @RequestBody PasswordChangeDto passwordChangeDto
    ){
        return trainerService.changePassword(username, passwordChangeDto.getNewPassword(), passwordChangeDto.getOldPassword());
    }


    @Operation(summary = "Update trainer status", description = "Activates or deactivates a trainer's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer status successfully updated"),
            @ApiResponse(responseCode = "404", description = "Trainer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateTrainerStatus(@PathVariable String username, @Valid @RequestBody StatusUpdateDTO statusUpdateDTO){
        trainerService.activate(username, statusUpdateDTO.getActive());
        return ResponseEntity.ok().build();
    }
}
