package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.LoginDto;
import com.javabootcamp.gym.data.dto.PasswordChangeDto;
import com.javabootcamp.gym.security.services.SecurityService;
import com.javabootcamp.gym.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/account")
@Tag(name = "Account endpoint", description = "Account actions")
public class AccountController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public AccountController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping("login")
    @Operation(summary = "Logs the user in")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, @NotNull BindingResult binding) {
        LOGGER.info("Post: /account/login");
        if (binding.hasErrors()) {
            LOGGER.info("Invalid request body format");
            var errors = handleValidationErrors(binding);
            return new ResponseEntity<>(errors, UNAUTHORIZED);
        }

        var r = securityService.authenticate(loginDto);

        if (r == null)
            return handleServerError();

        var response = r.isSuccess() ? ResponseEntity.ok(r) : new ResponseEntity<>(r.error(), UNAUTHORIZED);

        return response;
    }

    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDto viewModel, @NotNull BindingResult binding, Authentication auth) {
        LOGGER.info("PUT: /account/change-password");
        if (binding.hasErrors()) {
            LOGGER.info("Invalid request body format");
            var errors = handleValidationErrors(binding);
            return new ResponseEntity<>(errors, UNAUTHORIZED);
        }

        var match = securityService.matchUsernameResponse(auth, viewModel.username(),
                () -> changePassword(viewModel),
                () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }

    @PatchMapping("{username}/status")
    public ResponseEntity<?> setIsActiveStatus(@NotNull @PathVariable String username, @RequestParam(name = "active", defaultValue = "false") boolean isActive, Authentication auth) {
        LOGGER.info("PATCH: /account/{}/status", username);
        var match = securityService.matchUsernameResponse(auth, username,
                () -> {
                    var r = userService.setIsActive(username, isActive);

                    var code = r.map(aBoolean -> aBoolean ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).orElse(HttpStatus.NOT_FOUND);

                    return new ResponseEntity<>(code);
                },
                () -> {
                    LOGGER.info("Resource access is FORBIDDEN");
                    return new ResponseEntity<>(FORBIDDEN);
                });

        return match.get();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        var r = securityService.logout(authentication,
                () -> {
                    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
                    logoutHandler.logout(request, response, authentication);

                    return ResponseEntity.ok();
                },
                ResponseEntity::internalServerError);

        return r.get().build();
    }

    @NotNull
    private ResponseEntity<?> changePassword(PasswordChangeDto viewModel) {
        var result = userService.changePassword(viewModel.username(), viewModel.oldPassword(), viewModel.newPassword());

        var response = result ? ResponseEntity.status(OK).build() : ResponseEntity.status(BAD_REQUEST).body("Check passwords");

        return response;
    }

    @Override
    protected ResponseEntity<?> handleServerError() {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Cannot perform operation at the time");
    }
}
