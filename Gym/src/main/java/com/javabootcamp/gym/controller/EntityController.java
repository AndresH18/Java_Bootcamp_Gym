package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.services.IUpdateService;
import com.javabootcamp.gym.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class EntityController extends BaseController {
    protected final UserService userService;

    public EntityController(UserService userService) {
        this.userService = userService;
    }

    protected <T> ResponseEntity<?> update(String username, T dto, BindingResult binding, IUpdateService<T> updateService, IGetProfileController profileController) {
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            return ResponseEntity.badRequest().body(errors);
        }

        var b = updateService.update(username, dto);
        if (!b) return ResponseEntity.internalServerError().body("There was an error while updating the entity");

        return profileController.getProfile(username);
    }
}
