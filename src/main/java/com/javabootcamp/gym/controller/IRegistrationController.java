package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.viewmodels.RegistrationViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IRegistrationController<T extends RegistrationViewModel> {
    ResponseEntity<?> register(T t, BindingResult binding);
}
