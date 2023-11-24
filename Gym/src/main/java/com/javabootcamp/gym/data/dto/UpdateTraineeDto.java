package com.javabootcamp.gym.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UpdateTraineeDto(
//        @NotBlank
//        @NotNull
//        String username,
        @NotBlank
        @NotNull
        String firstName,
        @NotBlank
        @NotNull
        String lastName,
        @DateTimeFormat(pattern = "yyyy-M-d")
        @Past
        LocalDate dateOfBirth,
        String address,
        boolean isActive
) implements IUserDto
//        , IUsernameDto
{
}
