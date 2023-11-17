package com.javabootcamp.gym.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRepository extends JpaRepository<JwtModel, String> {

}
