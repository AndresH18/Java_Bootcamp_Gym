package com.javabootcamp.gym.security.data;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "jwt_tokens", schema = "security")
public class JwtSecurityToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String signature;
    private LocalDate expiration;
    @Column(name = "is_revoked")
    private boolean revoked;

    public JwtSecurityToken(String signature, LocalDate expiration, boolean revoked) {
        this.signature = signature;
        this.expiration = expiration;
        this.revoked = revoked;
    }

    public JwtSecurityToken(String signature, LocalDate expiration) {
        this(signature, expiration, false);
        this.signature = signature;
        this.expiration = expiration;
    }

    public JwtSecurityToken() {
    }

    public String getSignature() {
        return signature;
    }

    public JwtSecurityToken setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public JwtSecurityToken setExpiration(LocalDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
