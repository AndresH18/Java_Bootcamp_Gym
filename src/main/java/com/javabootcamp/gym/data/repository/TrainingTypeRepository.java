package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
}
