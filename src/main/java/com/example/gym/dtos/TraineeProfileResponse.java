package com.example.gym.dtos;


import java.time.LocalDate;
import java.util.List;

public class TraineeProfileResponse {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;

    private List<TrainerInfo> trainers;

    public TraineeProfileResponse() {
    }

    public TraineeProfileResponse(String firstName, String lastName, LocalDate dateOfBirth, String address, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
    }

    public static class TrainerInfo{
        public TrainerInfo(String username, String firstName, String lastName, String specialization) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.specialization = specialization;
        }

        private String username;
        private String firstName;
        private String lastName;
        private String specialization;



        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<TrainerInfo> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<TrainerInfo> trainers) {
        this.trainers = trainers;
    }
}
