package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.controller.IUpdateController;
import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.PasswordChangeViewModel;
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

    protected HttpStatus setIsActiveStatus(@NotNull String username, boolean isActive) {
        var r = userService.setIsActive(username, isActive);
//        if (r.isEmpty())
//            return HttpStatus.NOT_FOUND;
//
//        return r.get() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

        return r.map(aBoolean -> aBoolean ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).orElse(HttpStatus.NOT_FOUND);

    }

    protected Map<String, String> handleValidationErrors(@NotNull BindingResult bindingResult) {

        var errors = new HashMap<String, String>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }
}
