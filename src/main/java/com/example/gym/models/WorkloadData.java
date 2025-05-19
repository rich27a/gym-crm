package com.example.gym.models;

import java.time.LocalDate;

public interface WorkloadData {
    String getTrainerUsername();
    String getTrainerFirstName();
    String getTrainerLastName();
    boolean isActive();
    LocalDate getTrainingDate();
    int getTrainingDuration();
    ActionType getActionType();
}
