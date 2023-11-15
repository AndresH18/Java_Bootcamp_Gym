package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.model.TrainingType;
import com.javabootcamp.gym.data.repository.TrainingTypeRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainingTypeService {
    private final Logger logger = LoggerFactory.getLogger(TrainingTypeService.class);
    private final TrainingTypeRepository repository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository repository) {
        this.repository = repository;
    }

    @NotNull
    public Optional<List<TrainingType>> getAll() {
        try {
            return Optional.of(repository.findAll());
        } catch (Exception e) {
            logger.error("Failed to get TrainingTypes", e);
            return Optional.empty();
        }
    }

    @NotNull
    public Optional<List<TrainingType>> getAll(int page, int size) {
        try {
            if (size <= 0)
                size = 10;

            if (page < 0)
                page = 0;
            else
                page -= 1;

            Pageable pageable = PageRequest.of(page, size);
            var p = repository.findAll(pageable);

            var list = p.get().toList();

            return Optional.of(list);
        } catch (Exception e) {
            logger.error("Failed to get TrainingTypes", e);
            return Optional.empty();
        }
    }
}
