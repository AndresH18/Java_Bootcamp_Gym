package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.model.Trainer;
import com.javabootcamp.gym.data.viewmodels.BaseController;
import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.PasswordChangeViewModel;
import com.javabootcamp.gym.data.viewmodels.TrainerRegistrationViewModel;
import com.javabootcamp.gym.services.TrainerService;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController extends BaseController implements IRegistrationController<TrainerRegistrationViewModel> {

    private final TrainerService trainerService;

    public TrainerController(UserService userService, TrainerService trainerService) {
        super(userService);
        this.trainerService = trainerService;
    }

    @Override
    @PostMapping
    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> register(@Valid @RequestBody TrainerRegistrationViewModel trainerRegistrationViewModel, @NotNull BindingResult binding) {
        var response = new HashMap<String, Object>();
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            response.put("errors", errors);

            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        var trainer = trainerService.create(trainerRegistrationViewModel);
        if (trainer == null || trainer.getUser() == null)
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);

        var user = trainer.getUser();

        return ResponseEntity.ok(new LoginViewModel(user.getUsername(), user.getPassword()));
    }

    @GetMapping("login")
    public HttpStatus login(@Valid @RequestBody LoginViewModel loginViewModel, @NotNull BindingResult binding) {
        if (binding.hasErrors())
            return UNAUTHORIZED;

        return super.login(loginViewModel);
    }

    @PutMapping("change-password")
    public HttpStatus changePassword(@Valid @RequestBody PasswordChangeViewModel viewModel, @NotNull BindingResult binding) {
        if (binding.hasErrors())
            return BAD_REQUEST;

        return super.changePassword(viewModel);
    }
}
//
//class TrainerService {
//    Trainer create(TrainerRegistrationViewModel vm) {
//        return null;
//    }
//}
