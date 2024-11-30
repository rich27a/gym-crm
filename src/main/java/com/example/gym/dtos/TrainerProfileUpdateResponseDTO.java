package com.example.gym.dtos;

import java.util.List;

public class TrainerProfileUpdateResponseDTO extends TrainerProfileResponse{
    private String username;

    public TrainerProfileUpdateResponseDTO(String firstName, String lastName, String specialization, boolean isActive, List<TraineeInfo> trainees, String username) {
        super(firstName, lastName, specialization, isActive, trainees);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
