package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.TrainingTypeDto;
import com.javabootcamp.gym.services.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-types")
public class TrainingTypeController {
    private final TrainingTypeService service;

    @Autowired
    public TrainingTypeController(TrainingTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {

        var o = service.getAll();

        var list = o.map(TrainingTypeDto::convert);

        return list.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().build());
    }
}
