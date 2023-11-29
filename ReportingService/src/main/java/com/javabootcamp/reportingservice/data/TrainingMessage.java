package com.javabootcamp.reportingservice.data;

public record TrainingMessage(
        String trainerUsername,
        String trainerFirstName,
        String trainerLastName,
        boolean active,
        int duration,
        String date,
        boolean delete
) {
}
