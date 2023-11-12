package com.javabootcamp.gym.controller;

import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.viewmodels.AbstractBaseController;
import com.javabootcamp.gym.data.viewmodels.LoginViewModel;
import com.javabootcamp.gym.data.viewmodels.TraineeRegistrationViewModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestController
@RequestMapping("/trainee")
public class TraineeController extends AbstractBaseController implements IRegistrationController<TraineeRegistrationViewModel> {

    // TODO: inject from constructor
    private final TraineeService traineeService = new TraineeService();

    @PostMapping
    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> register(@Valid @RequestBody TraineeRegistrationViewModel viewModel, BindingResult binding) {

        var response = new HashMap<String, Object>();

        if (binding.hasErrors()) {
            var errors = handleValidationErrors(binding);
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, BAD_REQUEST);
        }

        var trainee = traineeService.create(viewModel);

        if (trainee == null || trainee.getUser() == null) {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        var user = trainee.getUser();

        return ResponseEntity.ok(new LoginViewModel(user.getUsername(), user.getPassword()));
    }
}

// TODO: delete testing class
class TraineeService {
    public Trainee create(TraineeRegistrationViewModel vm) {

        var t = new Trainee(1, vm.getDateOfBirth(), vm.getAddress());
        t.setUser(new User(1, vm.getFirstName(), vm.getLastName(), vm.getFirstName() + "." + vm.getLastName(), "1234567890", true));
        return t;
    }
}
