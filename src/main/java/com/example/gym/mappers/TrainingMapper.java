package com.example.gym.mappers;

import com.example.gym.dtos.TrainingInfoResponseDTO;
import com.example.gym.models.Training;
import com.example.gym.models.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(source = "trainingType", target = "trainingType")
    @Mapping(source = "trainingDate", target = "trainingDate", dateFormat = "yyyy-MM-dd")
    TrainingInfoResponseDTO toTrainingInfoResponse(Training training);

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
