package com.example.gym.mappers;

import com.example.gym.dtos.TrainingInfoResponseDTO;
import com.example.gym.dtos.TrainingRequestDTO;
import com.example.gym.dtos.TrainingTypeDTO;
import com.example.gym.models.Trainer;
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
    @Mapping(source = "trainer", target = "trainerName")
    @Mapping(source = "name", target = "trainingName")
    @Mapping(source = "trainingDate", target = "trainingDate", dateFormat = "yyyy-MM-dd")
    TrainingInfoResponseDTO toTrainingInfoResponse(Training training);

    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    @Mapping(source = "trainingName", target = "name")
    Training toEntity(TrainingRequestDTO request);


    @Mapping(source = "id", target = "trainingTypeId")
    TrainingTypeDTO toTrainingTypeDTO(TrainingType trainingType);

    default String mapSpecialization(TrainingType specialization) {
        return specialization != null ? specialization.getTrainingType().name() : "UNKNOWN";
    }

    default String mapTrainerName(Trainer trainer){
        return trainer != null ? trainer.getFirstName() + " " + trainer.getLastName() : "UNKNOWN";
    }

    default LocalDate mapDateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
