package com.javabootcamp.gym.data.viewmodels;

import jakarta.validation.constraints.NotBlank;

public class TrainerRegistrationViewModel extends RegistrationViewModel {
    @NotBlank
    private String specialization;
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
