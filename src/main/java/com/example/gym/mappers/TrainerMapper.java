package com.example.gym.mappers;

import com.example.gym.dtos.*;
import com.example.gym.models.Trainer;
import com.example.gym.models.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    @Mapping(source = "trainees", target = "trainees")
    @Mapping(source = "trainingType", target = "specialization")
    TrainerProfileResponse toResponse(Trainer trainer);


    @Mapping(source = "trainingType", target = "specialization")
    TrainerInfoResponseDTO toTrainerInfoResponse(Trainer trainer);

    @Mapping(source = "trainees", target = "trainees")
    @Mapping(source = "trainingType", target = "specialization")
    TrainerProfileUpdateResponseDTO toUpdateResponse(Trainer trainer);


    TrainerRegistrationResponseDTO toRegistrationResponse(Trainer trainer);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "specialization", target = "trainingType")
    Trainer toTrainerFromRequest(TrainerRegistrationRequestDTO trainerRequest);



    default String mapSpecialization(TrainingType specialization) {
        return specialization != null ? specialization.getTrainingType().name() : "UNKNOWN";
    }

    default TrainingType mapSpecialization(String specialization) {

        return TrainingType.fromString(specialization);
    }
}
