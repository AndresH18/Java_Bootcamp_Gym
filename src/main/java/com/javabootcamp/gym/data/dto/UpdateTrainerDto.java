package com.javabootcamp.gym.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

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
