package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
}
