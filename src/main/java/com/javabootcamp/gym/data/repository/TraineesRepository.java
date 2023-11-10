package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineesRepository extends JpaRepository<Trainee, Integer> {
}
