package com.javabootcamp.gym.data.viewmodels;

import jakarta.validation.constraints.NotBlank;

public class RegistrationViewModel {
    @NotBlank(message = "firstname must not be empty")
    private String firstName;
    @NotBlank(message = "lastname must not be empty")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
