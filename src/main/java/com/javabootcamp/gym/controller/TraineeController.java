package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.TraineeTrainingDto;
import com.javabootcamp.gym.data.dto.TrainingFilterDto;
import com.javabootcamp.gym.data.dto.UpdateTraineeDto;
import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.PasswordChangeViewModel;
import com.javabootcamp.gym.data.viewmodels.TraineeRegistrationViewModel;
import com.javabootcamp.gym.services.TraineeService;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/trainee")
public class TraineeController extends BaseController implements IRegistrationController<TraineeRegistrationViewModel>, IGetProfileController<Trainee>, IUpdateController {

    private final TraineeService traineeService;

    public TraineeController(UserService userService, TraineeService traineeService) {
        super(userService);
        this.traineeService = traineeService;
    }

    @PostMapping
    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> register(@Valid @RequestBody TraineeRegistrationViewModel viewModel, @NotNull BindingResult binding) {

        var response = new HashMap<String, Object>();

        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, BAD_REQUEST);
        }

        var trainee = traineeService.create(viewModel);

        if (trainee == null || trainee.getUser() == null) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        var user = trainee.getUser();

        return ResponseEntity.ok(new LoginViewModel(user.getUsername(), user.getPassword()));
    }

    @GetMapping("{username}")
    @Override
    public ResponseEntity<Trainee> getProfile(@PathVariable String username) {
        if (username == null)
            return ResponseEntity.badRequest().build();

        var trainee = traineeService.getByUsername(username);

        if (trainee == null)
            return new ResponseEntity<>(NOT_FOUND);


        return new ResponseEntity<>(trainee, OK);
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
    public HttpStatus setIsActiveStatus(@NotNull @PathVariable String username, @RequestParam(name = "isActive", defaultValue = "false") boolean isActive) {
        return super.setIsActiveStatus(username, isActive);
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateTrainee(@Valid @RequestBody UpdateTraineeDto dto, BindingResult binding) {
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            return ResponseEntity.badRequest().body(errors);
        }

        var b = traineeService.update(dto);
        if (!b)
            return ResponseEntity.internalServerError().build();

        return getProfile(dto.username());
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