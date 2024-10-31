package com.example.gym.models;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "trainees")
public class Trainee extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Date dateOfBirth;
    @Column
    private String address;
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    public Trainee() {
    }

    public Trainee(Long id, Date dateOfBirth, String address) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
//        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}
