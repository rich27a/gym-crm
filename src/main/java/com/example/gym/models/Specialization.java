package com.example.gym.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Specialization {
    CARDIO,
    STRENGTH_TRAINING,
    YOGA,
    CROSSFIT,
    HIIT;
    @JsonCreator
    public static Specialization fromString(String value) {
        return Specialization.valueOf(value.toUpperCase());
    }
}
