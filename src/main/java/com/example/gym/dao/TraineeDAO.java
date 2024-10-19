package com.example.gym.dao;

import com.example.gym.models.Storage;
import com.example.gym.models.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TraineeDAO implements GenericDAO<Trainee> {
    @Autowired
    private Map<Integer, Trainee> traineeMap;
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);
    @Override
    public void save(Trainee trainee) {
        traineeMap.put(trainee.getId(), trainee);
        logger.info("trainee: " + trainee.getId() + " successfully saved");
    }

    @Override
    public Optional<Trainee> findById(int id) {
        Trainee trainee = traineeMap.get(id);
        if(trainee==null)
            logger.warn("trainee: " + id + " not found");
        return Optional.ofNullable(trainee);
    }

    @Override
    public List<Trainee> findAll() {
        List<Trainee> traineeList = new ArrayList<>(traineeMap.values());
        traineeList.sort(new Comparator<Trainee>() {
            @Override
            public int compare(Trainee o1, Trainee o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });
        logger.info(traineeMap.size() + " : trainees has been found");
        return traineeList;
    }

    @Override
    public void delete(int id) {
        Trainee trainee = traineeMap.get(id);
        if(trainee == null){
            logger.warn("trainee: " + id + " not found");
        }else {
            traineeMap.remove(id);
            logger.info("trainee: " + id + " deleted");
        }
    }

    @Override
    @Autowired
    public void setStorage(Storage storage) {
        logger.info("setting storage...");
//        this.storage = storage;
    }
}
