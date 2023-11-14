package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.model.TrainingType;
import com.javabootcamp.gym.services.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<TrainingType>> getTrainingTypes(@RequestParam(name = "page", defaultValue = "1") int page,
                                                               @RequestParam(name = "page-size", defaultValue = "10") int size) {


        var o = service.getAll(page, size);

//        if (o.isEmpty())
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        return ResponseEntity.ok(o.get());
        return o.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    }
}
