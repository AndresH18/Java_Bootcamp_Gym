package com.javabootcamp.gym.services;

import com.javabootcamp.gym.data.model.TrainingType;
import com.javabootcamp.gym.helper.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrainingTypeService {

    @NotNull
    public Result<List<TrainingType>, Void> getAll() {
        return Result.value(List.of(TrainingType.values()));
    }
}
