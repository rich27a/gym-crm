package com.example.gym.models;

import jakarta.persistence.*;

@Entity
@Table(name = "trainers")
public class Trainer{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trainer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingType trainingType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Trainer() {
    }

    public Trainer(Long id, TrainingType trainingType, User user) {
        this.id = id;
        this.trainingType = trainingType;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
