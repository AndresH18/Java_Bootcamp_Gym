package com.javabootcamp.reportingservice.data;

public record TrainingMessage(
        String trainerUsername,
        String trainerFirstName,
        String trainerLastName,
        boolean active,
        Integer duration,
        String year,
        String month,
        boolean delete
) {

    public TrainingMessage {
        year = year.toUpperCase();
        month = month.toUpperCase();
    }
}
