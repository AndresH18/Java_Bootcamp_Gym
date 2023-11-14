package com.javabootcamp.gym.data.dto;

import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 *
 * @param periodFrom
 * @param periodTo
 * @param name
 * @param trainingName
 */
public record TrainingFilterDto(
        @DateTimeFormat(pattern = "yyyy-M-d")
        @Past(message = "Date must be in the past")
        LocalDate periodFrom,

        @DateTimeFormat(pattern = "yyyy-M-d")
        @Past(message = "Date must be in the past")
        LocalDate periodTo,

        String name,
        String trainingName
) {
}
