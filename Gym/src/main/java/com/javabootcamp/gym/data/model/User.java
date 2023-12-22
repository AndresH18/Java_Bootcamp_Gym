package com.javabootcamp.gym.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "Users", schema = "dbo")
public class User implements IModel, UserDetails {

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
    @Column(name = "password_hash")
    private String passwordHash = "";
    @Transient
    private String plainPassword;
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

    public User(int id, @NotNull String firstName, @NotNull String lastName, @NotNull String username, @NotNull String passwordHash, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.isActive = isActive;
    }

    public User(@NotNull String firstName, @NotNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(@NotNull String firstName, @NotNull String lastName, @NotNull String username, @NotNull String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
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
        return passwordHash;
    }

    public void setPassword(@NotNull String password) {
        this.passwordHash = password;
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

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public enum Role {
        TRAINER,
        TRAINEE
    }

    // region UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    // endregion
}
