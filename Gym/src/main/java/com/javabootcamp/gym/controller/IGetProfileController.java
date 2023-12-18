package com.javabootcamp.gym.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface IGetProfileController {
    ResponseEntity<?> getProfile(String username);

}
