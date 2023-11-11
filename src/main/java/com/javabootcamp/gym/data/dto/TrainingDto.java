package com.javabootcamp.gym.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public record TrainingDto(
        @NotBlank
        String traineeUsername,
        @NotBlank
        String trainerUsername,
        @NotBlank
        String trainingName,
        @DateTimeFormat(pattern = "yyyy-M-d")
        @NotNull
        LocalDate date,
        @Min(0)
        int duration
) {
}