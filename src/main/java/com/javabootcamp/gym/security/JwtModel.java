package com.javabootcamp.gym.security;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "jwt", schema = "security")
public class JwtModel {
    @Id
    private String signature;

    private LocalDate expiration;

    @Column(name = "is_valid")
    private boolean isValid;

    public JwtModel(String signature, LocalDate expiration, boolean isValid) {
        this.signature = signature;
        this.expiration = expiration;
        this.isValid = isValid;
    }

    public JwtModel() {
    }

    public String getSignature() {
        return signature;
    }

    public JwtModel setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public JwtModel setExpiration(LocalDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public boolean isValid() {
        return isValid;
    }

    public JwtModel setValid(boolean valid) {
        isValid = valid;
        return this;
    }
}
