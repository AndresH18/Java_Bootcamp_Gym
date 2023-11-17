package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.LoginDto;
import com.javabootcamp.gym.data.dto.PasswordChangeDto;
import com.javabootcamp.gym.services.security.SecurityService;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public AccountController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, @NotNull BindingResult binding) {
        if (binding.hasErrors()) return new ResponseEntity<>(UNAUTHORIZED);
//
//        var authenticated = userService.authenticate(loginDto.username(), loginDto.password());
//
//        var code = authenticated ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
//
//        return new ResponseEntity<>(code);

        var r = securityService.authenticate(loginDto);

        var response = r != null ? ResponseEntity.ok(r) : new ResponseEntity<>(UNAUTHORIZED);

        return response;
    }

    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDto viewModel, @NotNull BindingResult binding, Authentication auth) {
        if (binding.hasErrors()) return new ResponseEntity<>(UNAUTHORIZED);

        var match = securityService.matchUsernameResponse(auth, viewModel.username(),
                () -> {
                    var result = userService.changePassword(viewModel.username(), viewModel.oldPassword(), viewModel.newPassword());

                    var code = result ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

                    return new ResponseEntity<>(code);
                },
                () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }


    @PatchMapping("{username}/status")
    public ResponseEntity<?> setIsActiveStatus(@NotNull @PathVariable String username, @RequestParam(name = "active", defaultValue = "false") boolean isActive, Authentication auth) {

        var match = securityService.matchUsernameResponse(auth, username,
                () -> {

                    var r = userService.setIsActive(username, isActive);
//        if (r.isEmpty())
//            return HttpStatus.NOT_FOUND;
//
//        return r.get() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
                    var code = r.map(aBoolean -> aBoolean ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).orElse(HttpStatus.NOT_FOUND);

                    return new ResponseEntity<>(code);
                },
                () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }
}
