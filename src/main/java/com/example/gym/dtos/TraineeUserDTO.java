package com.example.gym.dtos;

import com.example.gym.models.Trainee;
import com.example.gym.models.User;

import java.util.Date;

public class TraineeUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    private Date dateOfBirth;
    private String address;
    private Long userId;

    public TraineeUserDTO() {
    }

    public TraineeUserDTO(Trainee trainee){
        this.firstName = trainee.getUser().getFirstName();
        this.lastName = trainee.getUser().getLastName();
        this.username = trainee.getUser().getUsername();
        this.password = trainee.getUser().getPassword();
        this.isActive = trainee.getUser().isActive();
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();
        this.userId = trainee.getUser().getId();
    }

    public TraineeUserDTO(String firstName, String lastName, String username, String password, boolean isActive, Date dateOfBirth, String address, Long userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
