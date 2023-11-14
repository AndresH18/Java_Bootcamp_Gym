package com.javabootcamp.gym.controller;

import org.springframework.http.HttpStatus;

public interface IUpdateController {
    HttpStatus setIsActiveStatus(String username, boolean isActive);
}
