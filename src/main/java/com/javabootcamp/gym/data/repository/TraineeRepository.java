package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
}
