package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.*;
import com.javabootcamp.gym.data.viewmodels.TraineeRegistrationViewModel;
import com.javabootcamp.gym.services.TraineeService;
import com.javabootcamp.gym.security.services.SecurityService;
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
public class TraineeController extends BaseController implements IRegistrationController<TraineeRegistrationViewModel>, IGetProfileController<TraineeProfileDto> {

    private final TraineeService traineeService;
    private final SecurityService securityService;

    public TraineeController(UserService userService, TraineeService traineeService, SecurityService securityService) {
        super(userService);
        this.traineeService = traineeService;
        this.securityService = securityService;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody TraineeRegistrationViewModel viewModel, @NotNull BindingResult binding) {

        var response = new HashMap<String, Object>();

        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        var trainee = traineeService.create(viewModel);

        if (trainee == null || trainee.getUser() == null) {
            return ResponseEntity.internalServerError().build();
        }
        var user = trainee.getUser();

        return ResponseEntity.ok(new LoginDto(user.getUsername(), user.getPlainPassword()));
//        return ResponseEntity.ok(new LoginDto(user.getUsername(), user.getPassword()));
    }

    @GetMapping("{username}")
    @Override
    public ResponseEntity<TraineeProfileDto> getProfile(@PathVariable String username) {
        if (username == null) return ResponseEntity.badRequest().build();

        var trainee = traineeService.getByUsername(username);

        if (trainee == null) return new ResponseEntity<>(NOT_FOUND);

        var dto = TraineeProfileDto.convert(trainee);

        return new ResponseEntity<>(dto, OK);
    }

    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TraineeTrainingDto>> getTrainings(@PathVariable String username,
                                                                 @Valid @ModelAttribute TrainingFilterDto filterDto,
                                                                 BindingResult binding,
                                                                 Authentication authentication) {

        return securityService.matchUsername(authentication, username,
                () -> {
                    if (binding.hasErrors()) {
                        // BAD_REQUEST
                        return ResponseEntity.badRequest().build();
                    }
                    var o = traineeService.getTrainings(username, filterDto);

                    return ResponseEntity.of(o);
                },
                () -> new ResponseEntity<>(FORBIDDEN));
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateTrainee(@PathVariable String username,
                                           @Valid @RequestBody UpdateTraineeDto dto,
                                           BindingResult binding,
                                           Authentication authentication) {

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
                },
                () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }

    @PutMapping("{username}/trainers")
    public ResponseEntity<?> updateTrainers(@PathVariable String username, @RequestBody List<String> trainers, Authentication authentication) {

        var match = securityService.matchUsernameResponse(authentication, username,
                () -> {
                    // TODO: set trainee (username) trainers (list of usernames)
                    //  For the trainee, use the list of usernames that belong to trainers and set the trainee' trainers
                    return ResponseEntity.ok().build();
                },
                () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }
}