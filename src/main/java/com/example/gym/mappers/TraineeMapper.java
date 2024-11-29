package com.example.gym.mappers;

import com.example.gym.dtos.TraineeProfileResponse;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "trainerList", target = "trainers")
    TraineeProfileResponse toResponse(Trainee trainee);

    @Mapping(source = "trainingType", target = "specialization")
    TraineeProfileResponse.TrainerInfo toTrainerInfo(Trainer trainer);

    default String mapSpecialization(TrainingType specialization) {
        return specialization != null ? specialization.getTrainingType().name() : null;
    }

    default LocalDate mapDateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
