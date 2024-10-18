package com.example.gym.models;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private Map<Integer, Trainer> trainerMap = new HashMap<>();
    private Map<Integer, Trainee> traineeMap = new HashMap<>();
    private Map<Integer, Training> trainingMap = new HashMap<>();

    public Map<Integer, Trainer> getTrainerMap() {
        return trainerMap;
    }

    public Map<Integer, Trainee> getTraineeMap() {
        return traineeMap;
    }

    public Map<Integer, Training> getTrainingMap() {
        return trainingMap;
    }
}
