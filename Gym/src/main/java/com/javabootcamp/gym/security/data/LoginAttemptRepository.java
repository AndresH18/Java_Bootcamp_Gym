package com.javabootcamp.gym.security.data;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    List<LoginAttempt> findByUsernameAndAttemptTimeAfterAndSuccessIsFalse(@NotNull String username, LocalDateTime time);
}
