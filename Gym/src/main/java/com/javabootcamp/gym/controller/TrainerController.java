package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.LoginDto;
import com.javabootcamp.gym.data.dto.TrainerProfileDto;
import com.javabootcamp.gym.data.dto.TrainingFilterDto;
import com.javabootcamp.gym.data.dto.UpdateTrainerDto;
import com.javabootcamp.gym.data.viewmodels.TrainerRegistrationViewModel;
import com.javabootcamp.gym.security.services.SecurityService;
import com.javabootcamp.gym.services.TrainerService;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/trainers")
public class TrainerController extends EntityController implements IRegistrationController<TrainerRegistrationViewModel>, IGetProfileController {

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
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            var response = new HashMap<String, Object>();
            response.put("errors", errors);

            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        var result = trainerService.create(trainerRegistrationViewModel);

        if (result.isSuccess()) {
            var user = result.value().getUser();
            return ResponseEntity.ok(new LoginDto(user.getUsername(), user.getPlainPassword()));
        }

        return ResponseEntity.internalServerError().body(result.error());
    }

    @Override
    @GetMapping("{username}")
    public ResponseEntity<TrainerProfileDto> getProfile(@PathVariable String username) {
        if (username == null) return ResponseEntity.badRequest().build();

        var result = trainerService.getByUsername(username);
        if (result.isError())
            return ResponseEntity.notFound().build();

        var dto = TrainerProfileDto.convert(result.value());

        return ResponseEntity.ok(dto);
    }

    @SuppressWarnings("DuplicatedCode")
    @GetMapping("{username}/trainings")
    public ResponseEntity<?> getTrainings(@PathVariable String username, @Valid @ModelAttribute TrainingFilterDto filterDto,
                                          BindingResult binding, Authentication authentication) {
        return securityService.matchUsername(authentication, username, () -> {
            if (binding.hasErrors())
                return ResponseEntity.badRequest().build();
            var r = trainerService.getTrainings(username, filterDto);
            return r.match(ResponseEntity::ok,
                    unused -> ResponseEntity.internalServerError().body("Could not retrieve trainings Error"));
        }, () -> new ResponseEntity<>(FORBIDDEN));
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateTrainer(@PathVariable String username, @Valid @RequestBody UpdateTrainerDto dto,
                                           BindingResult binding, Authentication authentication) {

        var match = securityService.matchUsernameResponse(authentication, username, () ->
                        super.update(username, dto, binding, trainerService, this),
                () -> new ResponseEntity<>(FORBIDDEN));

        return match.get();
    }
}