package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.LoginDto;
import com.javabootcamp.gym.data.dto.TraineeProfileDto;
import com.javabootcamp.gym.data.dto.TrainingFilterDto;
import com.javabootcamp.gym.data.dto.UpdateTraineeDto;
import com.javabootcamp.gym.data.viewmodels.TraineeRegistrationViewModel;
import com.javabootcamp.gym.security.services.SecurityService;
import com.javabootcamp.gym.services.TraineeService;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/trainees")
public class TraineeController extends EntityController implements IRegistrationController<TraineeRegistrationViewModel>, IGetProfileController {

    private final TraineeService traineeService;
    private final SecurityService securityService;

    public TraineeController(UserService userService, TraineeService traineeService, SecurityService securityService) {
        super(userService);
        this.traineeService = traineeService;
        this.securityService = securityService;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody TraineeRegistrationViewModel viewModel, @NotNull BindingResult binding) {
        if (binding.hasErrors()) {
            var response = new HashMap<String, Object>();
            var errors = handleValidationErrors(binding);
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        var result = traineeService.create(viewModel);

        if (result.isSuccess()) {
            var user = result.value().getUser();
            return ResponseEntity.ok(new LoginDto(user.getUsername(), user.getPlainPassword()));
        }
        return ResponseEntity.internalServerError().body(result.error());
    }

    @GetMapping("{username}")
    @Override
    public ResponseEntity<?> getProfile(@PathVariable String username) {
        if (username == null) return ResponseEntity.badRequest().build();

        var result = traineeService.getByUsername(username);
        if (result.isError())
            return ResponseEntity.notFound().build();

        var dto = TraineeProfileDto.convert(result.value());

        return new ResponseEntity<>(dto, OK);
    }

    @SuppressWarnings("DuplicatedCode")
    @GetMapping("{username}/trainings")
    public ResponseEntity<?> getTrainings(@PathVariable String username, @Valid @ModelAttribute TrainingFilterDto filterDto,
                                          BindingResult binding, Authentication authentication) {
        return securityService.matchUsername(authentication, username, () -> {
            if (binding.hasErrors())
                return ResponseEntity.badRequest().build();
            var r = traineeService.getTrainings(username, filterDto);
            return r.match(ResponseEntity::ok,
                    unused -> ResponseEntity.internalServerError().body("Could not retrieve trainings Error"));
        }, () -> new ResponseEntity<>(FORBIDDEN));
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateTrainee(@PathVariable String username, @Valid @RequestBody UpdateTraineeDto dto,
                                           BindingResult binding, Authentication authentication) {

        var match = securityService.matchUsernameResponse(authentication, username,
                () -> super.update(username, dto, binding, traineeService, this),
                () -> new ResponseEntity<>(FORBIDDEN));
        return match.get();
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> delete(@PathVariable String username, Authentication authentication) {
        if (username == null) return ResponseEntity.badRequest().build();

        var match = securityService.matchUsernameResponse(authentication, username, () -> {
            var r = traineeService.delete(username);
            if (r.isEmpty()) return ResponseEntity.notFound().build();

            var code = r.get() ? OK : INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(code);
        }, () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }

    @PutMapping("{username}/trainers")
    public ResponseEntity<?> updateTrainers(@PathVariable String username, @RequestBody List<String> trainers, Authentication authentication) {

        var match = securityService.matchUsernameResponse(authentication, username, () -> {
            // TODO: set trainee (username) trainers (list of usernames)
            //  For the trainee, use the list of usernames that belong to trainers and set the trainee' trainers
            return new ResponseEntity<>(NOT_IMPLEMENTED);
        }, () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }
}