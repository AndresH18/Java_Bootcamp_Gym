package com.javabootcamp.gym.security.data;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface JwtRepository extends JpaRepository<JwtSecurityToken, String> {

    Optional<JwtSecurityToken> findBySignature(@NotNull String signature);

    Optional<JwtSecurityToken> findBySignatureAndRevokedIsFalseAndExpirationIsBefore(@NotNull String signature, @NotNull LocalDate expiration);

    Optional<JwtSecurityToken> findBySignatureAndRevokedIsFalse(@NotNull String signature);

    boolean existsBySignatureAndRevokedIsFalse(@NotNull String signature);
}
