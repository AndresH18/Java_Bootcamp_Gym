package com.javabootcamp.gym.data.dto;

import com.javabootcamp.gym.data.model.Trainee;

import java.time.LocalDate;
import java.util.List;

public record TraineeProfileDto(String firstName, String lastName, LocalDate dateOfBirth, String address,
                                boolean isActive, List<TraineeProfileDto.Trainer> trainers) {

    record Trainer(String username, String firstName, String lastName, String specialization) {
    }

    public static TraineeProfileDto convert(Trainee trainee) {
        var trainers = trainee.getTrainers().stream().map(t -> {
            var u = t.getUser();
            return new Trainer(u.getUsername(), u.getFirstName(), u.getLastName(), t.getSpecialization().toString());
        }).toList();

        var u = trainee.getUser();
        return new TraineeProfileDto(u.getFirstName(), u.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(), u.isActive(), trainers);
    }
}
