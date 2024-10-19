package com.example.gym.dao;

import com.example.gym.models.Storage;
import com.example.gym.models.Trainer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;

public class TrainerDAO implements GenericDAO<Trainer>{
    private Storage storage;
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAO.class);
    @Override
    public void save(Trainer trainer) {
        storage.getTrainerMap().put(trainer.getId(), trainer);
        logger.info("trainer with id: " + trainer.getId() + " has been successfully saved");
    }

    @Override
    public Trainer findById(int id) {
        logger.info("trainer with id: " + id + " has been found");
        return storage.getTrainerMap().get(id);
    }

    @Override
    public List<Trainer> findAll() {
        List<Trainer> trainerList = new ArrayList<>(storage.getTrainerMap().values());
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
        storage.getTrainerMap().remove(id);
        logger.info("deleting trainer with id: " + id);
    }

    @Override
    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
