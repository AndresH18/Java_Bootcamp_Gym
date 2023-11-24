package com.javabootcamp.gym.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeDto(@NotBlank String username,
                                @NotBlank
                                      @Size(min = 10, max = 10, message = "Password must have length 10")
                                      String oldPassword,
                                @NotBlank
                                      @Size(min = 10, max = 10, message = "Password must have length 10")
                                      String newPassword) {
}
