package com.example.gym.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainers")
public class Trainer extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trainer_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingType trainingType;

    @Column
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trainer_user_id")
    @JsonManagedReference
    private List<Training> trainingList = new ArrayList<>();

//    @ManyToMany(mappedBy = "trainers")
//    private List<Trainee> traineeList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    private List<Trainer> trainees = new ArrayList<>();

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    public Trainer() {
    }

    public Trainer(Long id, TrainingType trainingType) {
        this.id = id;
        this.trainingType = trainingType;
//        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public List<Training> getTrainingList() {
        return trainingList;
    }

    public void setTrainingList(List<Training> trainingList) {
        this.trainingList = trainingList;
    }

    public List<Trainer> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<Trainer> trainees) {
        this.trainees = trainees;
    }
    //    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}
