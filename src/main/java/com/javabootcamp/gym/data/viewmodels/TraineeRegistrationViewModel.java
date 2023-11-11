package com.javabootcamp.gym.data.viewmodels;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TraineeRegistrationViewModel extends RegistrationViewModel {

    @DateTimeFormat(pattern = "yyyy-M-d")
    @Past(message = "Date must be in the past")
    @NotNull
    private LocalDate dateOfBirth;

    private String address;

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
