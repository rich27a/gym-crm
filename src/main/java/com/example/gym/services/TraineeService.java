package com.example.gym.services;

import com.example.gym.models.Specialization;
import com.example.gym.models.Trainee;
import com.example.gym.models.Training;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.utils.Profile;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;

    public TraineeService(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
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
        return traineeRepository.findByUsername(username)
                .map(trainee -> {
                    traineeRepository.delete(trainee);
                    logger.info("Trainee with username {} successfully deleted", username);
                    return true;
                })
                .orElse(false);
    }

    public List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, Specialization trainingType) {
        return traineeRepository.findByUsername(username)
                .map(trainee -> trainee.getTrainingList().stream()
                        .filter(training ->

                                (fromDate == null || !training.getTrainingDate().isBefore(fromDate)) &&
                                        (toDate == null || !training.getTrainingDate().isAfter(toDate)) &&
                                        (trainerName == null || training.getTrainer().getUsername().equals(trainerName)) &&
                                        (trainingType == null || training.getTrainingType().getTrainingType().equals(trainingType))
                        )
                        .collect(Collectors.toList())
                )
                .orElseThrow(() -> new RuntimeException("Trainee not found with username: " + username));
    }

    public Optional<Trainee> selectTraineeProfile(Long id) {
        return traineeRepository.findById(id);
    }
    public List<Trainee> getTrainees(){
        return traineeRepository.findAll();
    }


}
