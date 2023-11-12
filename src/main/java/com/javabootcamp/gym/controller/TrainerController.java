package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.model.Trainer;
import com.javabootcamp.gym.data.viewmodels.AbstractBaseController;
import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.TrainerRegistrationViewModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/trainer")
public class TrainerController extends AbstractBaseController implements IRegistrationController<TrainerRegistrationViewModel> {

    // TODO: inject from constructor
    private final TrainerService trainerService = new TrainerService();

    @Override
    @PostMapping
    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> register(@Valid @RequestBody TrainerRegistrationViewModel trainerRegistrationViewModel, BindingResult binding) {
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
}

class TrainerService {
    Trainer create(TrainerRegistrationViewModel vm) {
        return null;
    }
}
