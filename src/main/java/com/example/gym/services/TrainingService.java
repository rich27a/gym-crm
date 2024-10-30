package com.example.gym.services;

import com.example.gym.dao.TrainingDAO;
import com.example.gym.models.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;
    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public Training createTrainingProfile(int trainingId, int traineeId, int trainerId, String trainingName, String trainingType, String trainingDate, int trainingDuration) {
        logger.debug("Creating Training profile with name: {}", trainingName);
        Training training = new Training();
//        training.setTrainingId(trainingId);
//        training.setTraineeId(traineeId);
//        training.setTrainerId(trainerId);
//        training.setTrainingName(trainingName);
//        training.setTrainingType(trainingType);
//        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        trainingDAO.save(training);
        logger.debug("Training profile created: {}", training);
        return training;
    }
    public Optional<Training> selectTrainingProfile(int id) {
        logger.debug("Selecting Training profile with id: {}", id);
        return trainingDAO.findById(id);
    }

}
