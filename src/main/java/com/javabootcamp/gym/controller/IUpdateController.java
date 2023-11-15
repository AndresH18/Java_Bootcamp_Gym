package com.javabootcamp.gym.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface IUpdateController {
    ResponseEntity<?> setIsActiveStatus(String username, boolean isActive);
}
