package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.TrainingDto;
import com.javabootcamp.gym.services.TrainingService;
import com.javabootcamp.gym.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("trainings")
public class TrainingController extends BaseController {

    private final TrainingService service;

    @Autowired
    public TrainingController(UserService userService, TrainingService service) {
        super(userService);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addTraining(@Valid @RequestBody TrainingDto dto, BindingResult binding) {
        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            return new ResponseEntity<>(errors, BAD_REQUEST);
        }

        var training = service.create(dto);

        if (training == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok().build();

    }


}
