package com.example.gym.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainingTypes")
public class TrainingType {

    @Id
    @Column(name = "trainingType_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String trainingType;


    @Column
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trainingType_id")
    private List<Trainee> traineeList = new ArrayList<>();

    public TrainingType() {
    }

    public TrainingType(Long id, String trainingType, List<Trainee> traineeList) {
        this.id = id;
        this.trainingType = trainingType;
        this.traineeList = traineeList;
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

    public List<Trainee> getTraineeList() {
        return traineeList;
    }

    public void setTraineeList(List<Trainee> traineeList) {
        this.traineeList = traineeList;
    }
}
