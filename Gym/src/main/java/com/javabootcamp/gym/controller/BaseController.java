package com.javabootcamp.gym.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected BaseController() {
    }

    protected Map<String, String> handleValidationErrors(@NotNull BindingResult bindingResult) {

        var errors = new HashMap<String, String>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }

    protected ResponseEntity<?> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    protected ResponseEntity<?> handleServerError() {
        return ResponseEntity.internalServerError().build();
    }
}
