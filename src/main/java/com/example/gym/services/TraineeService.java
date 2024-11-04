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
    public Trainee findTraineeByUsername(String username){
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
        Long id = trainee.getId();
        Optional<Trainee> traineeOpt = traineeRepository.findById(id);
        if (traineeOpt.isPresent()) {
            logger.debug("updating trainee with id: {}" , id);
            return Optional.of(traineeRepository.save(trainee));
        }else {
            logger.warn("trainee with id: " + id + " not found");
            return traineeOpt;
        }
    }

    @Transactional
    public boolean deleteTraineeByUsername(String username) {
        Trainee trainee = traineeRepository.findByUsername(username);
        traineeRepository.delete(trainee);
//        if(trainee.isPresent()){
//            traineeRepository.delete(trainee.get());
//            logger.info("Trainee with username {} succesfully deleted", username);
//            return true;
//        }else {
//            logger.warn("Trainee with username {} not found. No deletion performed");
//            return false;
//        }
        return true;
    }

    public Optional<Trainee> selectTraineeProfile(Long id) {
        return traineeRepository.findById(id);
    }
    public List<Trainee> getTrainees(){
        return traineeRepository.findAll();
    }


}
