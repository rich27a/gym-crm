package com.example.gym.dao;

import com.example.gym.models.Storage;
import com.example.gym.models.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class TrainingDAO implements GenericDAO<Training> {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAO.class);
    private Storage storage;
    @Override
    public void save(Training training) {
        storage.getTrainingMap().put(training.getTrainingId(), training);
        logger.info("training: " + training.getTrainingId() + "successfully saved");
    }

    @Override
    public Optional<Training> findById(int id) {
        Training training = storage.getTrainingMap().get(id);
        if(training == null){
            logger.warn("training with id: " + id + " not found");
        }else {
            logger.info("training with id: " + id +" has been found");
        }
        return Optional.ofNullable(training);
    }

    @Override
    public List<Training> findAll() {
        logger.warn("operation not supported");
        return null;
    }

    @Override
    public void delete(int id) {
        logger.warn("operation not supported");
    }

    @Override
    @Autowired
    public void setStorage(Storage storage) {
        logger.info("setting storage...");
        this.storage = storage;
    }
}
