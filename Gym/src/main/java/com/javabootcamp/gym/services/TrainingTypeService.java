package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.model.TrainingType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainingTypeService {

    @NotNull
    public Optional<List<TrainingType>> getAll() {
        return Optional.of(List.of(TrainingType.values()));
    }
}
