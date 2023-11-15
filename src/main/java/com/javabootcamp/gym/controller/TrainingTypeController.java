package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.dto.TrainingTypeDto;
import com.javabootcamp.gym.services.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-type")
public class TrainingTypeController {
    private final TrainingTypeService service;

    @Autowired
    public TrainingTypeController(TrainingTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                  @RequestParam(name = "page-size", defaultValue = "10") int size) {

        var o = service.getAll(page, size);

        var list = o.map(TrainingTypeDto::convert);

        return list.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().build());
    }
}
