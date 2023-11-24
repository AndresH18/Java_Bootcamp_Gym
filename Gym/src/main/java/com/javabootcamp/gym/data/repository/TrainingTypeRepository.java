package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.TrainingType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {

    Optional<TrainingType> findFirstByNameIgnoreCase(@NotNull String name);
}
