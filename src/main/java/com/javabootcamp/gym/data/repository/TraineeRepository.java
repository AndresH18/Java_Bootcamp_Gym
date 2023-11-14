package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Trainee;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    Optional<Trainee> findFirstByUserUsername(@NotNull String username);
}
