package com.example.gym.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainingTypes")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Specialization trainingType;

    public TrainingType() {
    }

    public TrainingType(Long id, Specialization trainingType) {
        this.id = id;
        this.trainingType = trainingType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Specialization getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(Specialization trainingType) {
        this.trainingType = trainingType;
    }


}
