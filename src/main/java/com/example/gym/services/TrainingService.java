package com.example.gym.services;

import com.example.gym.dtos.TrainingRequestDTO;
import com.example.gym.dtos.TrainingTypeDTO;
import com.example.gym.mappers.TrainingMapper;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.repositories.TrainerRepository;
import com.example.gym.repositories.TrainingRepository;
import com.example.gym.repositories.TrainingTypeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.gym.utils.TransactionContext.getTransactionId;

@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingMapper trainingMapper;
    private final WorkloadNotificationService workloadNotificationService;


    public TrainingService(TrainingRepository trainingRepository, TrainerRepository trainerRepository, TraineeRepository traineeRepository, TrainingTypeRepository trainingTypeRepository, TrainingMapper trainingMapper, WorkloadNotificationService workloadNotificationService) {
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingMapper = trainingMapper;
        this.workloadNotificationService = workloadNotificationService;
    }

    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Transactional
    public Optional<Boolean> createTraining(TrainingRequestDTO requestTraining) {
        logger.debug("Creating Training with name: {}", requestTraining.getTrainingName());
        Trainer trainer = trainerRepository.findByUsername(requestTraining.getTrainerUsername()).orElseThrow(() -> new IllegalArgumentException("trainer not valid"));
        Trainee trainee = traineeRepository.findByUsername(requestTraining.getTraineeUsername()).orElseThrow(() -> new IllegalArgumentException("trainee not valid"));

        Training training = trainingMapper.toEntity(requestTraining);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        Training savedTraining = trainingRepository.save(training);
        workloadNotificationService.notifyTrainingAdded(savedTraining, getTransactionId());
        return Optional.of(true);
    }

    @Transactional
    public void deleteTraining(Long trainingId){
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found"));

        workloadNotificationService.notifyTrainingDeleted(training, getTransactionId());

        trainingRepository.delete(training);

        logger.info("[Transaction: {}] Successfully deleted training with ID: {}",
                getTransactionId(), trainingId);
    }

    @Transactional
    public List<TrainingTypeDTO> getAllTrainingTypes(){
        logger.debug("Getting all training-types");
        return trainingTypeRepository.findAll()
                .stream().map(trainingType -> trainingMapper.toTrainingTypeDTO(trainingType))
                .toList();
    }


}
