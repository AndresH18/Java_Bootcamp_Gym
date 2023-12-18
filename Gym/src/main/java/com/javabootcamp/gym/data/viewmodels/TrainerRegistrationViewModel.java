package com.javabootcamp.gym.data.viewmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TrainerRegistrationViewModel extends RegistrationViewModel {
    @NotBlank
    @Pattern(regexp = "(?i)FITNESS|YOGA|ZUMBA|STRETCHING|RESISTANCE")
    private String specialization;
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
