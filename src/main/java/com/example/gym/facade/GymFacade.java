package com.example.gym.facade;

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

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainerProfile(trainer);
    }

    public Optional<Trainer> updateTrainer(Trainer trainer) {
        return trainerService.updateTrainerProfile(trainer);
    }

    public Optional<Trainer> getTrainer(int id) {
        return trainerService.selectTrainerProfile(id);
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTraineeProfile(trainee);
    }

    public Optional<Trainee> updateTrainee(Trainee trainee) {
        return traineeService.updateTraineeProfile(trainee);
    }

    public void deleteTrainee(String username) {
        traineeService.deleteTraineeByUsername(username);
    }

    public Optional<Trainee> getTrainee(Long id) {
        return traineeService.selectTraineeProfile(id);
    }

    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }
    public Optional<Training> getTraining(Long id) {
        return trainingService.selectTrainingProfile(id);
    }
}
