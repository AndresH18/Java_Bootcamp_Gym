package com.javabootcamp.gym.data.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Trainers")
public class Trainer implements IModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TrainingType specialization;

    @OneToMany(mappedBy = "trainer")
    private Set<Training> trainings;

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
}
