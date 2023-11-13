package com.javabootcamp.gym.data.viewmodels;

import com.javabootcamp.gym.services.user.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected final UserService userService;

    protected BaseController(UserService userService) {
        this.userService = userService;
    }

    protected HttpStatus login(@NotNull LoginViewModel loginViewModel) {
        var authenticated = userService.authenticate(loginViewModel.username(), loginViewModel.password());

        return authenticated
                ? HttpStatus.OK
                : HttpStatus.UNAUTHORIZED;
    }

    protected HttpStatus changePassword(@NotNull PasswordChangeViewModel viewModel) {
        var result = userService.changePassword(viewModel.username(), viewModel.oldPassword(), viewModel.newPassword());

        return result
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
    }

    protected Map<String, String> handleValidationErrors(@NotNull BindingResult bindingResult) {

        var errors = new HashMap<String, String>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }
}
