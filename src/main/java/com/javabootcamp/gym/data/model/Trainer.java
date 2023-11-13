package com.javabootcamp.gym.data.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Trainers", schema = "dbo")
public class Trainer implements IModel, ICopy<Trainer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TrainingType specialization;

//    @OneToMany(mappedBy = "trainer")
//    private Set<Training> trainings;

//    @ManyToMany(mappedBy = "trainers")
//    private Set<Trainee> trainees;

    public Trainer(int id, TrainingType specialization, User user) {
        this(specialization, user);
        this.id = id;
    }

    public Trainer(TrainingType specialization, User user) {
        this.user = user;
        this.specialization = specialization;
    }

    public Trainer() {
    }

    @Override
    public Trainer copyFrom(Trainer trainer) {
        this.specialization = trainer.specialization;
        // copy other fields here
        return this;
    }

    // TODO: equals(), hashcode()

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

//    public Set<Training> getTrainings() {
//        return trainings;
//    }
//
//    public void setTrainings(Set<Training> trainings) {
//        this.trainings = trainings;
//    }

//    public Set<Trainee> getTrainees() {
//        return trainees;
//    }
//
//    public void setTrainees(Set<Trainee> trainees) {
//        this.trainees = trainees;
//    }
}
