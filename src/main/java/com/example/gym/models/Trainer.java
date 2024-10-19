package com.example.gym.models;

public class Trainer extends User{
    private Specialization specialization;

    public Trainer(){

    }

    public Trainer(int id, String firstName, String lastName, String username, String password, boolean isActive, Specialization specialization) {
        super(id, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }


    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", isActive=" + isActive() +
                "specialization=" + specialization +
                '}';
    }
}
