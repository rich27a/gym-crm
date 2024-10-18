package com.example.gym.models;

public class TrainingType {
    private String trainingType;

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "trainingType='" + trainingType + '\'' +
                '}';
    }
}
