package com.javabootcamp.gym.data.viewmodels;

import jakarta.validation.constraints.NotBlank;

public class TrainerRegistrationViewModel extends RegistrationViewModel {
    private int specializationId = 0;
    private String specialization;

    public int getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(int specializationId) {
        this.specializationId = specializationId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
