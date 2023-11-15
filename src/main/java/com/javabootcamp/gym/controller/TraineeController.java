package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.*;
import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.PasswordChangeViewModel;
import com.javabootcamp.gym.data.viewmodels.TraineeRegistrationViewModel;
import com.javabootcamp.gym.services.TraineeService;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/trainees")
public class TraineeController extends BaseController implements IRegistrationController<TraineeRegistrationViewModel>, IGetProfileController<TraineeProfileDto>, IUpdateController {

    private final TraineeService traineeService;

    public TraineeController(UserService userService, TraineeService traineeService) {
        super(userService);
        this.traineeService = traineeService;
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody TraineeRegistrationViewModel viewModel, @NotNull BindingResult binding) {

        var response = new HashMap<String, Object>();

        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
//                    new ResponseEntity<Map<String, Object>>(response, BAD_REQUEST);
        }

        var trainee = traineeService.create(viewModel);

        if (trainee == null || trainee.getUser() == null) {
            return ResponseEntity.internalServerError().build();
        }
        var user = trainee.getUser();

        return ResponseEntity.ok(new LoginDto(user.getUsername(), user.getPassword()));
    }

    @GetMapping("{username}")
    @Override
    public ResponseEntity<TraineeProfileDto> getProfile(@PathVariable String username) {
        if (username == null)
            return ResponseEntity.badRequest().build();

        var trainee = traineeService.getByUsername(username);

        if (trainee == null)
            return new ResponseEntity<>(NOT_FOUND);

        var dto = TraineeProfileDto.convert(trainee);

        return new ResponseEntity<>(dto, OK);
    }

    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TraineeTrainingDto>> getTrainers(@PathVariable String username, @Valid @ModelAttribute TrainingFilterDto filterDto, BindingResult binding) {
        if (binding.hasErrors() || username == null) {
            // BAD_REQUEST
            return ResponseEntity.badRequest().build();
        }
        var o = traineeService.getTrainings(username, filterDto);

        return ResponseEntity.of(o);
    }

    @PatchMapping("{username}/status")
    @Override
    public ResponseEntity<?> setIsActiveStatus(@NotNull @PathVariable String username, @RequestParam(name = "active", defaultValue = "false") boolean isActive) {
        return super.setIsActiveStatus(username, isActive);
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateTrainee(@PathVariable String username, @Valid @RequestBody UpdateTraineeDto dto, BindingResult binding) {
        return super.update(username, dto, binding, traineeService, this);
    }

    @GetMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginViewModel loginViewModel, @NotNull BindingResult binding) {
        if (binding.hasErrors())
            return new ResponseEntity<>(UNAUTHORIZED);

        return super.login(loginViewModel);
    }

    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeViewModel viewModel, @NotNull BindingResult binding) {
        if (binding.hasErrors())
            return new ResponseEntity<>(UNAUTHORIZED);

        return super.changePassword(viewModel);
    }
}