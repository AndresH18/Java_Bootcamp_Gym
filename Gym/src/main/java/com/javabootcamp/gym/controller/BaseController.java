package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.services.IUpdateService;
import com.javabootcamp.gym.services.user.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected final UserService userService;

    protected BaseController(UserService userService) {
        this.userService = userService;
    }

    protected Map<String, String> handleValidationErrors(@NotNull BindingResult bindingResult) {

        var errors = new HashMap<String, String>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }

    protected <T, R> ResponseEntity<?> update(String username, T dto, BindingResult binding, IUpdateService<T> updateService, IGetProfileController<R> profileController) {
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            return ResponseEntity.badRequest().body(errors);
        }

        var b = updateService.update(username, dto);
        if (!b) return ResponseEntity.internalServerError().build();

        return profileController.getProfile(username);
    }
}
