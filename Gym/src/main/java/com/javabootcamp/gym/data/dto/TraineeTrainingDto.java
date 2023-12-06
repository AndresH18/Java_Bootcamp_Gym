package com.javabootcamp.gym.data.dto;

import java.time.LocalDate;

public record TraineeTrainingDto(String trainingName,
                                 LocalDate trainingDate,
                                 String trainingType,
                                 int trainingDuration,
                                 String trainerName) {
}
