package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.TrainingTypeDto;
import com.javabootcamp.gym.services.TrainingTypeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-types")
public class TrainingTypeController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeController.class);
    private final TrainingTypeService service;

    @Autowired
    public TrainingTypeController(TrainingTypeService service) {
        this.service = service;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource found"),
            @ApiResponse(responseCode = "500", description = "Could not access resource")
    })
    public ResponseEntity<?> getTrainingTypes() {
        LOGGER.info("Received GET request.");

        var result = service.getAll();

        return result.match(trainingTypes -> {
            var list = TrainingTypeDto.convert(trainingTypes);

            return ResponseEntity.ok(list);
        }, unused -> handleNotFound());
    }

    @Override
    protected ResponseEntity<?> handleNotFound() {
        LOGGER.error("GET: Failed to get training types.");
        return ResponseEntity.internalServerError().body(
                "Unable to get training types from server. Something unexpected happened");
    }
}
