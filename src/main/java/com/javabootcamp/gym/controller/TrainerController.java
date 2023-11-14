package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.*;
import com.javabootcamp.gym.data.model.Trainer;
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
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/trainers")
public class TrainerController extends BaseController implements IRegistrationController<TrainerRegistrationViewModel>, IGetProfileController<TrainerProfileDto> {

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

    @Override
    @GetMapping("{username}")
    public ResponseEntity<TrainerProfileDto> getProfile(@PathVariable String username) {
        if (username == null)
            return ResponseEntity.badRequest().build();

        var trainer = trainerService.getByUsername(username);

        if (trainer == null)
            return new ResponseEntity<>(NOT_FOUND);

        var dto = TrainerProfileDto.convert(trainer);

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("{username}/status")
    @Override
    public HttpStatus setIsActiveStatus(@NotNull @PathVariable String username, @RequestParam(name = "isActive", defaultValue = "false") boolean isActive) {
        return super.setIsActiveStatus(username, isActive);
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateTrainee(@PathVariable String username, @Valid @RequestBody UpdateTrainerDto dto, BindingResult binding) {
        return super.update(username, dto, binding, trainerService, this);
    }

    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TrainerTrainingDto>> getTrainers(@PathVariable String username, @Valid @ModelAttribute TrainingFilterDto filterDto, BindingResult binding) {
        if (binding.hasErrors() || username == null) {
            // BAD_REQUEST
            return ResponseEntity.badRequest().build();
        }
        var o = trainerService.getTrainings(username, filterDto);

        return ResponseEntity.of(o);
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