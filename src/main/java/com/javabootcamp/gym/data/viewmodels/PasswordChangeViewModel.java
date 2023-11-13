package com.javabootcamp.gym.data.viewmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeViewModel(@NotBlank String username,
                                      @NotBlank
                                      @Size(min = 10, max = 10, message = "Password must have length 10")
                                      String oldPassword,
                                      @NotBlank
                                      @Size(min = 10, max = 10, message = "Password must have length 10")
                                      String newPassword) {
}
