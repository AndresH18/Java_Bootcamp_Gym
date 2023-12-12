package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.*;
import com.javabootcamp.gym.data.viewmodels.TrainerRegistrationViewModel;
import com.javabootcamp.gym.services.TrainerService;
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
@RequestMapping("/trainers")
public class TrainerController extends BaseController implements IRegistrationController<TrainerRegistrationViewModel>, IGetProfileController<TrainerProfileDto> {

    private final TrainerService trainerService;

    private final SecurityService securityService;

    public TrainerController(UserService userService, TrainerService trainerService, SecurityService securityService) {
        super(userService);
        this.trainerService = trainerService;
        this.securityService = securityService;
    }

    @Override
    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody TrainerRegistrationViewModel trainerRegistrationViewModel, @NotNull BindingResult binding) {
        var response = new HashMap<String, Object>();
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            response.put("errors", errors);

            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        var trainer = trainerService.create(trainerRegistrationViewModel);
        if (trainer == null || trainer.getUser() == null) return new ResponseEntity<>(INTERNAL_SERVER_ERROR);

        var user = trainer.getUser();

        return ResponseEntity.ok(new LoginDto(user.getUsername(), user.getPlainPassword()));
    }

    @Override
    @GetMapping("{username}")
    public ResponseEntity<TrainerProfileDto> getProfile(@PathVariable String username) {
        // NOTE: No need to check if user is authenticated because the filter checks for that.
        if (username == null) return ResponseEntity.badRequest().build();

        var trainer = trainerService.getByUsername(username);

        if (trainer == null) return new ResponseEntity<>(NOT_FOUND);

        var dto = TrainerProfileDto.convert(trainer);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateTrainee(@PathVariable String username,
                                           @Valid @RequestBody UpdateTrainerDto dto,
                                           BindingResult binding,
                                           Authentication authentication) {


        var match = securityService.matchUsernameResponse(authentication, username, () ->
                        super.update(username, dto, binding, trainerService, this),
                () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }

    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TrainerTrainingDto>> getTrainings(@PathVariable String username,
                                                                 @Valid @ModelAttribute TrainingFilterDto filterDto,
                                                                 BindingResult binding,
                                                                 Authentication authentication) {

        return securityService.matchUsername(authentication, username, () -> {
                    if (binding.hasErrors()) {
                        // BAD_REQUEST
                        return ResponseEntity.badRequest().build();
                    }
                    var o = trainerService.getTrainings(username, filterDto);

                    return ResponseEntity.of(o);
                },
                () -> new ResponseEntity<>(FORBIDDEN));
    }
}