package com.javabootcamp.gym.data.model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "Trainees")
public class Trainee implements IModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainee_id")
    private Set<Training> trainings;

    @Transient
    @Deprecated
    private int userId;

    public Trainee(int id, int userId, @NotNull LocalDate dateOfBirth, @NotNull String address) {
        this.id = id;
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(int userId, @NotNull LocalDate dateOfBirth, @NotNull String address) {
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trainee trainee = (Trainee) o;

        if (id != trainee.id) return false;
        if (userId != trainee.userId) return false;
        if (!dateOfBirth.equals(trainee.dateOfBirth)) return false;
        return address.equals(trainee.address);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + dateOfBirth.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }

    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }


    public @NotNull LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @NotNull String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Deprecated
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Deprecated
    public int getUserId() {
        return userId;
    }
}
