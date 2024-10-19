package com.example.gym.services;

import com.example.gym.dao.TrainerDAO;
import com.example.gym.models.Specialization;
import com.example.gym.models.Trainer;
import com.example.gym.utils.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerService {
    @Autowired
    private TrainerDAO trainerDAO;
    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public Trainer createTrainerProfile(String firstName, String lastName, Specialization specialization) {
        String username = Profile.generateUsername(firstName, lastName);
        String password = Profile.generatePassword();
        UUID uuid = UUID.randomUUID();
        int id = uuid.hashCode();
        logger.debug("generating id: {}", id);
        Trainer trainer = new Trainer(id, firstName, lastName, username, password, true, specialization);
        trainerDAO.save(trainer);
        logger.debug("trainer with id: {} ", id + " successfully created...");
        return trainer;
    }

    public Optional<Trainer> updateTrainerProfile(int id, String firstName, String lastName, Specialization specialization) {
        Optional<Trainer> trainerOpt = trainerDAO.findById(id);
        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();
            trainer.setFirstName(firstName);
            trainer.setLastName(lastName);
            trainer.setSpecialization(specialization);
            trainerDAO.save(trainer);
            logger.debug("trainee with id: {}" , id + " successfully updated");

        }else {
            logger.info("trainee with id: {}" , id + " not found");
        }
        return trainerOpt;
    }


    public Optional<Trainer> selectTrainerProfile(int id) {
        return trainerDAO.findById(id);
    }
}
