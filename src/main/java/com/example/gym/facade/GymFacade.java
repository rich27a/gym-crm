package com.example.gym.facade;

import com.example.gym.dtos.TraineeUserDTO;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.services.TraineeService;
import com.example.gym.services.TrainerService;
import com.example.gym.services.TrainingService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GymFacade {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public Trainer createTrainer(String firstName, String lastName, Specialization specialization) {
        return trainerService.createTrainerProfile(firstName, lastName, specialization);
    }

    public Optional<Trainer> updateTrainer(int id, String firstName, String lastName, Specialization specialization) {
        return trainerService.updateTrainerProfile(id, firstName, lastName, specialization);
    }

    public Optional<Trainer> getTrainer(int id) {
        return trainerService.selectTrainerProfile(id);
    }

    public TraineeUserDTO createTrainee(String firstName, String lastName, String dateOfBirth, String address) {
//        return traineeService.createTraineeProfile(firstName, lastName, dateOfBirth, address);
        return null;
    }

    public Optional<Trainee> updateTrainee(int id, String firstName, String lastName, String dateOfBirth, String address) {
        return traineeService.updateTraineeProfile(id, firstName, lastName, dateOfBirth, address);
    }

    public void deleteTrainee(int id) {
        traineeService.deleteTraineeProfile(id);
    }

    public Optional<Trainee> getTrainee(int id) {
        return traineeService.selectTraineeProfile(id);
    }

    public Training createTraining(int trainingId, int traineeId, int trainerId, String trainingName, String trainingType, String trainingDate, int trainingDuration) {
        return trainingService.createTrainingProfile(trainingId,traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
    }
    public Optional<Training> getTraining(int id) {
        return trainingService.selectTrainingProfile(id);
    }
}
