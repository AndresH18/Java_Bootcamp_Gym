package com.javabootcamp.gym.messaging;

public record TrainingMessage(
        String trainerUsername,
        String trainerFirstName,
        String trainerLastName,
        boolean active,
        int duration,
        String year,
        String month,
        boolean delete
) {
}
