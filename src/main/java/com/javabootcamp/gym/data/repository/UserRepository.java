package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
