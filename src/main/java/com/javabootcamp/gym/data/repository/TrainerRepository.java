package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
}
