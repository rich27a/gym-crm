package com.example.gym.utils;

import com.example.gym.models.Training;
import com.example.gym.models.User;

public class LineToModelParser {
    public static void parseLineToUser(User user, String[] data){
        user.setId(Integer.parseInt(data[1]));
        user.setFirstName(data[2]);
        user.setLastName(data[3]);
        user.setUsername(data[4]);
        user.setPassword(data[5]);
        user.setActive(Boolean.parseBoolean(data[6]));
    }
    public static void parseLineToTraining(Training training, String[] data){
        training.setTraineeId(Integer.parseInt(data[1]));
        training.setTrainerId(Integer.parseInt(data[2]));
        training.setTrainingName(data[3]);
        training.setTrainingType(data[4]);
        training.setTrainingDate(data[5]);
        training.setTrainingDuration(Integer.parseInt(data[6]));
    }

}
