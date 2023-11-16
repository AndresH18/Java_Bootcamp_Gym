package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.PasswordChangeViewModel;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeViewModel viewModel, @NotNull BindingResult binding) {
        if (binding.hasErrors()) return new ResponseEntity<>(UNAUTHORIZED);

        var result = userService.changePassword(viewModel.username(), viewModel.oldPassword(), viewModel.newPassword());

        var code = result ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(code);
    }

    @GetMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginViewModel loginViewModel, @NotNull BindingResult binding) {
        if (binding.hasErrors()) return new ResponseEntity<>(UNAUTHORIZED);

        var authenticated = userService.authenticate(loginViewModel.username(), loginViewModel.password());

        var code = authenticated ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

        return new ResponseEntity<>(code);
    }

    @PatchMapping("{username}/status")
    public ResponseEntity<?> setIsActiveStatus(@NotNull @PathVariable String username, @RequestParam(name = "active", defaultValue = "false") boolean isActive) {
        var r = userService.setIsActive(username, isActive);
//        if (r.isEmpty())
//            return HttpStatus.NOT_FOUND;
//
//        return r.get() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

        var code = r.map(aBoolean -> aBoolean ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).orElse(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(code);
    }
}
