package com.example.gym.services;

import com.example.gym.models.Trainee;
import com.example.gym.repositories.TraineeRepository;
import com.example.gym.utils.Profile;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Trainee> selectTraineeProfile(Long id) {
        return traineeRepository.findById(id);
    }
    public List<Trainee> getTrainees(){
        return traineeRepository.findAll();
    }


}
