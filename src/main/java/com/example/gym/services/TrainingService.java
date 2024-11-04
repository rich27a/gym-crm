package com.example.gym.services;

import com.example.gym.dao.TrainingDAO;
import com.example.gym.models.Training;
import com.example.gym.repositories.TrainingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Transactional
    public Training createTraining(Training training) {
        logger.debug("Creating Training with name: {}", training.getName());
        return trainingRepository.save(training);
    }
    @Transactional
    public Optional<Training> selectTrainingProfile(Long id) {
        logger.debug("Selecting Training profile with id: {}", id);
        return trainingRepository.findById(id);
    }

}
