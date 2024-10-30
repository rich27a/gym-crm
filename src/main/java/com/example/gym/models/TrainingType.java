package com.example.gym.models;

import jakarta.persistence.*;

@Entity
@Table(name = "trainingTypes")
public class TrainingType {

    @Id
    @Column(name = "trainingType_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String trainingType;

    public TrainingType() {
    }

    public TrainingType(Long id, String trainingType) {
        this.id = id;
        this.trainingType = trainingType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }
}
