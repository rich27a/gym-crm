package com.example.gym.dao;

import com.example.gym.models.Storage;
import com.example.gym.models.Trainer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDAO implements GenericDAO<Trainer>{

    @Autowired
    private Map<Integer, Trainer> trainerMap;
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);
    @Override
    public void save(Trainer trainer) {
        trainerMap.put(trainer.getId(), trainer);
        logger.info("trainer with id: " + trainer.getId() + " has been successfully saved");
    }

    @Override
    public Optional<Trainer> findById(int id) {
        logger.info("trainer with id: " + id + " has been found");
        return Optional.ofNullable(trainerMap.get(id));
    }

    @Override
    public List<Trainer> findAll() {
        List<Trainer> trainerList = new ArrayList<>(trainerMap.values());
        trainerList.sort(new Comparator<Trainer>() {
            @Override
            public int compare(Trainer o1, Trainer o2) {
                return o1.getFirstName().compareTo(o2.getFirstName());
            }
        });

        logger.info(trainerList.size() + " : trainers has been found");
        return trainerList;
    }

    @Override
    public void delete(int id) {
        logger.info("delete operation is not supported");
    }

    @Override
    @Autowired
    public void setStorage(Storage storage) {
        logger.info("setting storage...");
    }
}
