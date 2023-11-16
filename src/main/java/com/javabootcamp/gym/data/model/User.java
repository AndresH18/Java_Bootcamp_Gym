package com.javabootcamp.gym.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "Users", schema = "dbo")
public class User implements IModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "first_name")
    private String firstName;
    @NotNull
    @Column(name = "last_name")
    private String lastName;
    @NotNull
    private String username = "";
    @NotNull
    @JsonIgnore
    private String password = "";
    @Column(name = "is_active")
    @JsonIgnore
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToOne(mappedBy = "user"/*, cascade = CascadeType.REMOVE*/)
    @JsonBackReference
    private Trainee trainee;

    @OneToOne(mappedBy = "user"/*, cascade = CascadeType.REMOVE*/)
    @JsonBackReference
    private Trainer trainer;

    public User(int id, @NotNull String firstName, @NotNull String lastName, @NotNull String username, @NotNull String password, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public User(@NotNull String firstName, @NotNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(@NotNull String firstName, @NotNull String lastName, @NotNull String username, @NotNull String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public User() {
    }
    // TODO: equals(), hashcode()

    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public @NotNull String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    public @NotNull String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull String lastName) {
        this.lastName = lastName;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public User setTrainee(Trainee trainee) {
        this.trainee = trainee;
        return this;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public enum Role {
        TRAINER,
        TRAINEE
    }
}
