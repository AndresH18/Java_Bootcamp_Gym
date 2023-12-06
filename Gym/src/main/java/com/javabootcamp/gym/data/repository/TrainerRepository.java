package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Trainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findFirstByUserUsername(@NotNull String username);
}
