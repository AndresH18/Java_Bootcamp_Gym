package com.javabootcamp.gym.security.data;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<JwtSecurityToken, String> {

    Optional<JwtSecurityToken> findBySignature(@NotNull String signature);
}
