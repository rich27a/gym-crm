package com.example.gym.dtos;


import jakarta.validation.constraints.NotNull;

public class StatusUpdateDTO {
    @NotNull
    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
