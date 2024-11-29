package com.example.gym.dtos;

import java.time.LocalDate;

public class TraineeProfileUpdateResponseDTO extends TraineeProfileResponse{
    private String username;
    public TraineeProfileUpdateResponseDTO(String firstName, String lastName, LocalDate dateOfBirth, String address, boolean isActive, String username) {
        super(firstName, lastName, dateOfBirth, address, isActive);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
