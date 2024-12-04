package com.example.gym.services;

import com.example.gym.dtos.TrainerInfoResponseDTO;
import com.example.gym.dtos.TrainingInfoResponseDTO;
import com.example.gym.mappers.TrainerMapper;
import com.example.gym.mappers.TrainingMapper;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.repositories.TrainerRepository;
import com.example.gym.utils.Profile;
import jakarta.transaction.Transactional;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerService trainerService;
    private final PasswordEncoder passwordEncoder;
    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;

    public TraineeService(TraineeRepository traineeRepository, TrainerService trainerService, PasswordEncoder passwordEncoder, TrainerMapper trainerMapper, TrainerRepository trainerRepository, TrainingMapper trainingMapper) {
        this.traineeRepository = traineeRepository;
        this.trainerService = trainerService;
        this.passwordEncoder = passwordEncoder;
        this.trainerMapper = trainerMapper;
        this.trainerRepository = trainerRepository;
        this.trainingMapper = trainingMapper;
    }

    private static Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Transactional
    public Trainee createTraineeProfile(Trainee trainee) {
        logger.info("starting createTraineeProfile...");
        String username = Profile.generateUsername(trainee.getFirstName(),trainee.getLastName());
        String password = Profile.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);
        logger.info("saving trainee with id {}", trainee.getId());
        return traineeRepository.save(trainee);
    }

    @Transactional
    public Optional<Trainee> findTraineeByUsername(String username){
        logger.info("searching for trainee with username {}", username);
        return traineeRepository.findByUsername(username);
    }

    @Transactional
    public Optional<Trainee> findTraineeById(Long id){
        logger.info("searching for trainee with id {}", id);
        System.out.println(traineeRepository.findById(id));
        return traineeRepository.findById(id);
    }

    @Transactional
    public Optional<Trainee> updateTraineeProfile(Trainee trainee) {
        logger.info("searching trainee with id {}", trainee.getId());
        return traineeRepository.findById(trainee.getId())
                .map(existingTrainee -> {
                    existingTrainee.setFirstName(trainee.getFirstName());
                    existingTrainee.setLastName(trainee.getLastName());
                    existingTrainee.setDateOfBirth(trainee.getDateOfBirth());
                    existingTrainee.setAddress(trainee.getAddress());
                    existingTrainee.setUsername(Profile.generateUsername(existingTrainee.getFirstName(), existingTrainee.getLastName()));
                    return traineeRepository.save(existingTrainee);
                });
    }

    @Transactional
    public boolean deleteTraineeByUsername(String username) {
        logger.info("searching trainee with username: {}", username);

        return traineeRepository.findByUsername(username)
                .map(trainee -> {
                    traineeRepository.delete(trainee);
                    logger.info("Trainee with username {} successfully deleted", username);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<List<TrainingInfoResponseDTO>> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, Specialization trainingType) {
        logger.info("searching trainee with username: {}", username);

        return traineeRepository.findByUsername(username)
                .map(trainee -> trainee.getTrainingList().stream()
                        .filter(training ->
                                (fromDate == null || !training.getTrainingDate().isBefore(fromDate)) &&
                                        (toDate == null || !training.getTrainingDate().isAfter(toDate)) &&
                                        (trainerName == null || training.getTrainer().getUsername().equals(trainerName)) &&
                                        (trainingType == null || training.getTrainingType().getTrainingType().equals(trainingType))
                        )
                        .map(training -> trainingMapper.toTrainingInfoResponse(training))
                        .toList()
                );
    }
    @Transactional
    public Optional<List<TrainerInfoResponseDTO>> updateTraineeTrainerList(String username, List<String> trainersUsernames){
        logger.info("updating trainee with username: {}", username);
        return traineeRepository.findByUsername(username)
                .map(trainee -> {
                   List<Trainer> trainers = trainerRepository.findByUsernameIn(trainersUsernames);
                   if(trainers.size() != trainersUsernames.size()){
                       throw new IllegalArgumentException("One or more trainers not found");
                   }
                   trainee.setTrainerList(trainers);
                   traineeRepository.save(trainee);

                   return trainers.stream()
                           .map(trainer -> trainerMapper.toTrainerInfoResponse(trainer))
                           .toList();
                });
    }

    @Transactional
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        logger.info("searching trainee with username: {}", traineeUsername);
        return trainerService.getUnassignedTrainersFromTraineeUsername(traineeUsername);
    }
    @Transactional
    public Boolean changePassword(String username, String newPassword, String oldPassword){
        logger.info("updating trainee with username: {}", username);
        return traineeRepository.findByUsername(username).map(
                trainee -> {
                    if (passwordEncoder.matches(oldPassword, trainee.getPassword())) {
                        trainee.setPassword(passwordEncoder.encode(newPassword));
                        traineeRepository.save(trainee);
                        return true;
                    }else {
                        return false;
                    }
                }
        ).orElseGet(() -> false);
    }
    @Transactional
    public void activate(String username, Boolean isActive){
        logger.info("updating trainee with username: {}", username);
        traineeRepository.findByUsername(username)
                .map(trainer -> {
                    trainer.setActive(isActive);
                    return traineeRepository.save(trainer);
                }).orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
    }




}
