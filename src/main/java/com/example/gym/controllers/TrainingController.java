package com.example.gym.controllers;

import com.example.gym.dtos.TrainingRequestDTO;
import com.example.gym.dtos.TrainingTypeDTO;
import com.example.gym.services.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trainings", description = "APIs for managing training sessions and training types")
@RestController
@RequestMapping("/api/trainings")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Operation(
            summary = "Create a new training session",
            description = "Creates a new training session with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Training session created successfully",
                    content = @Content(schema = @Schema(implementation = TrainingRequestDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - User is not authorized to create training",
                    content = @Content(schema = @Schema(implementation = Boolean.class))
            )
    })
    @PostMapping
    public ResponseEntity<?> addTraining(@Valid @RequestBody TrainingRequestDTO training){
        return trainingService.createTraining(training)
                .map(created -> ResponseEntity.ok(created))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false));
    }
    @Operation(
            summary = "Get all training types",
            description = "Retrieves a list of all available training types"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved training types",
                    content = @Content(schema = @Schema(implementation = TrainingTypeDTO.class))
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No training types found"
            )
    })
    @GetMapping("/training-types")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes(){
        List<TrainingTypeDTO> trainingTypeDTOS = trainingService.getAllTrainingTypes();
        return trainingTypeDTOS.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(trainingTypeDTOS);
    }
    @Operation(
            summary = "Delete a training session",
            description = "Deletes a training session by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Training session deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Training session not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {

        trainingService.deleteTraining(id);

        return ResponseEntity.noContent().build();
    }
}
