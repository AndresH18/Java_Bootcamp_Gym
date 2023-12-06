package com.javabootcamp.gym.data.dto;

import com.javabootcamp.gym.data.model.Trainer;

import java.util.List;

public record TrainerProfileDto(String firstName, String lastName, String specialization, boolean isActive,
                                List<Trainee> trainees) {
    record Trainee(String username, String firstName, String lastName) {
    }

    public static TrainerProfileDto convert(Trainer trainer) {
        var trainees = trainer.getTrainees().stream().map(t -> {
            var u = t.getUser();
            return new Trainee(u.getUsername(), u.getFirstName(), u.getLastName());
        }).toList();

        var u = trainer.getUser();
        return new TrainerProfileDto(u.getFirstName(), u.getLastName(), trainer.getSpecialization().getName(), u.isActive(), trainees);
    }
}
