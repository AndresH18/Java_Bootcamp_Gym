package com.javabootcamp.gym.data.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UpdateTrainerDto(
//        @NotBlank
//        @NotNull
//        String username,
        @NotBlank
        @NotNull
        String firstName,
        @NotBlank
        @NotNull
        String lastName,
        @NotBlank
        String specialization,
        @PositiveOrZero
        int specializationId,
        boolean isActive
)
        implements IUserDto
//        , IUsernameDto
{
}
