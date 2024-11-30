package com.example.gym.mappers;

import com.example.gym.dtos.TrainerProfileResponse;
import com.example.gym.dtos.TrainerProfileUpdateResponseDTO;
import com.example.gym.models.Trainer;
import com.example.gym.models.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    @Mapping(source = "trainees", target = "trainees")
    @Mapping(source = "trainingType", target = "specialization")
    TrainerProfileResponse toResponse(Trainer trainer);

    @Mapping(source = "trainees", target = "trainees")
    @Mapping(source = "trainingType", target = "specialization")
    TrainerProfileUpdateResponseDTO toUpdateResponse(Trainer trainer);

    default String mapSpecialization(TrainingType specialization) {
        return specialization != null ? specialization.getTrainingType().name() : "UNKNOWN";
    }
}
