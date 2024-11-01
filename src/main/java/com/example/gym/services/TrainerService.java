package com.example.gym.services;

import com.example.gym.dao.TrainerDAO;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainer;
import com.example.gym.repositories.TrainerRepository;
import com.example.gym.utils.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

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

    public Optional<Trainer> updateTrainerProfile(int id, String firstName, String lastName, Specialization specialization) {
        return Optional.empty();
    }


    public Optional<Trainer> selectTrainerProfile(int id) {
        return Optional.empty();
    }
}
