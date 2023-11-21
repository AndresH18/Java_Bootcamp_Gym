package com.javabootcamp.gym.security.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempt", schema = "security")
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    @Column(name = "attempt_time")
    private LocalDateTime attemptTime;

    @Column(name = "success")
    private boolean success;

    public LoginAttempt(long id, String username, LocalDateTime attemptTime, boolean success) {
        this(username, attemptTime, success);
        this.id = id;
    }

    public LoginAttempt(String username, LocalDateTime attemptTime, boolean success) {
        this.username = username;
        this.attemptTime = attemptTime;
        this.success = success;
    }

    public LoginAttempt(String username, LocalDateTime attemptTime) {
        this(username, attemptTime, false);
    }

    public LoginAttempt() {
    }

    public long getId() {
        return id;
    }

    public LoginAttempt setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LoginAttempt setUsername(String username) {
        this.username = username;
        return this;
    }

    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }

    public LoginAttempt setAttemptTime(LocalDateTime attemptTime) {
        this.attemptTime = attemptTime;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public LoginAttempt setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
