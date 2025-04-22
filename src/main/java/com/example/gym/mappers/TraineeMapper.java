package com.example.gym.mappers;

import com.example.gym.dtos.*;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "trainerList", target = "trainers")
    TraineeProfileResponse toResponse(Trainee trainee);

    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "trainerList", target = "trainers")
    TraineeProfileUpdateResponseDTO toUpdatedResponse(Trainee trainee);

    @Mapping(source = "trainingType", target = "specialization")
    TraineeProfileResponse.TrainerInfo toTrainerInfo(Trainer trainer);

    @Mapping(target = "password", ignore = true)
    UserRegistrationResponseDTO toRegistrationResponse(Trainee trainee);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    Trainee toTraineeFromRegistrationRequest(TraineeRegistrationRequestDTO traineeRegistrationRequestDTO);

    TraineeRegistrationResponseDTO toTraineeRegistrationResponse(Trainee trainee);

    default String mapSpecialization(TrainingType specialization) {
        return specialization != null ? specialization.getTrainingType().name() : "UNKNOWN";
    }
    default LocalDate mapDateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
