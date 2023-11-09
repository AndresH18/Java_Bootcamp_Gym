package com.javabootcamp.gym.data.model;

import jakarta.persistence.*;

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

    @Transient
    @Deprecated
    private int userId;

    @Deprecated
    @Transient
    private int specializationId;

    public Trainer(int id, int userId, int specializationId) {
        this.id = id;
        this.userId = userId;
        this.specializationId = specializationId;
    }

    public Trainer(int userId, int specializationId) {
        this.userId = userId;
        this.specializationId = specializationId;
    }

    public Trainer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trainer trainer = (Trainer) o;

        if (id != trainer.id) return false;
        if (userId != trainer.userId) return false;
        return specializationId == trainer.specializationId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + specializationId;
        return result;
    }

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

    @Deprecated
    public int getSpecializationId() {
        return specializationId;
    }

    @Deprecated
    public void setSpecializationId(int specializationId) {
        this.specializationId = specializationId;
    }

    @Deprecated
    public int getUserId() {
        return userId;
    }

    @Deprecated
    public void setUserId(int userId) {
        this.userId = userId;
    }

}
