package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.controller.IUpdateController;
import com.javabootcamp.gym.data.dto.IUsernameDto;
import com.javabootcamp.gym.data.model.Trainer;
import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.PasswordChangeViewModel;
import com.javabootcamp.gym.services.IUpdateService;
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

    protected ResponseEntity<?> login(@NotNull LoginViewModel loginViewModel) {
        var authenticated = userService.authenticate(loginViewModel.username(), loginViewModel.password());

        var code = authenticated
                ? HttpStatus.OK
                : HttpStatus.UNAUTHORIZED;

        return new ResponseEntity<>(code);
    }

    protected ResponseEntity<?> changePassword(@NotNull PasswordChangeViewModel viewModel) {
        var result = userService.changePassword(viewModel.username(), viewModel.oldPassword(), viewModel.newPassword());

        var code = result
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(code);
    }

    protected ResponseEntity<?> setIsActiveStatus(@NotNull String username, boolean isActive) {
        var r = userService.setIsActive(username, isActive);
//        if (r.isEmpty())
//            return HttpStatus.NOT_FOUND;
//
//        return r.get() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

        var code = r.map(aBoolean -> aBoolean ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).orElse(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(code);
    }

    protected Map<String, String> handleValidationErrors(@NotNull BindingResult bindingResult) {

        var errors = new HashMap<String, String>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }

    protected <T, R> ResponseEntity<?> update(String username,
                                              T dto,
                                              BindingResult binding,
                                              IUpdateService<T> updateService,
                                              IGetProfileController<R> profileController) {
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            return ResponseEntity.badRequest().body(errors);
        }

        var b = updateService.update(username, dto);
        if (!b)
            return ResponseEntity.internalServerError().build();

        return profileController.getProfile(username);
    }
}
