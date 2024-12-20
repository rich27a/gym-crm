package com.example.gym.utils;

import com.example.gym.models.Training;
import com.example.gym.models.User;

public class LineToModelParser {
    public static void parseLineToUser(User user, String[] data){
//        user.setId(Integer.parseInt(data[1]));
        user.setFirstName(data[2]);
        user.setLastName(data[3]);
        user.setUsername(Profile.generateUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(Profile.generatePassword());
        user.setActive(Boolean.parseBoolean(data[6]));

    }
    public static void parseLineToTraining(Training training, String[] data){
//        training.setTrainingId(Integer.parseInt(data[1]));
//        training.setTraineeId(Integer.parseInt(data[2]));
//        training.setTrainerId(Integer.parseInt(data[3]));
//        training.setTrainingName(data[4]);
//        training.setTrainingType(data[5]);
//        training.setTrainingDate(data[6]);
        training.setTrainingDuration(Integer.parseInt(data[7]));
    }

}
