package com.example.gym.services;

import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.repositories.TrainerRepository;
import com.example.gym.utils.Profile;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Transactional
    public Trainer createTrainerProfile(Trainer trainer) {
        logger.info("starting createTraineeProfile...");
        String username = Profile.generateUsername(trainer.getFirstName(),trainer.getLastName());
        String password = Profile.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);
        logger.info("saving trainee with id {}", trainer.getId());
        return trainerRepository.save(trainer);
    }

    @Transactional
    public Optional<Trainer> updateTrainerProfile(Trainer trainer) {
        Long id = trainer.getId();
        logger.info("updating trainee with id: {}", id);
        Optional<Trainer> trainerOptional = trainerRepository.findById(id);
        if(trainerOptional.isPresent()){
            return Optional.of(trainerRepository.save(trainer));
        }else {
            logger.warn("trainer with id: {}", id + " not found");
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Trainer> findTrainerByUsername(String username){
        logger.info("searching for trainer with username {}", username);
        return trainerRepository.findByUsername(username);
    }
    @Transactional
    public boolean deleteTrainerByUsername(String username) {
        logger.info("Trainer with username {} successfully deleted", username);
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        if(trainer.isPresent()){
            trainerRepository.delete(trainer.get());
            return true;
        }else {
            logger.warn("Trainer with username {} not found. No deletion performed");
            return false;
        }
    }


    public Optional<Trainer> selectTrainerProfile(int id) {
        return Optional.empty();
    }
}
