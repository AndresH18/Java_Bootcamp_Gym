package com.javabootcamp.gym.data.dto;

import java.time.LocalDate;

public record TrainerTrainingDto(String trainingName,
                                 LocalDate trainingDate,
                                 String trainingType,
                                 int trainingDuration,
                                 String traineeName) {
        }