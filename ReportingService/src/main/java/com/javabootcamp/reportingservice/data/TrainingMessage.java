package com.javabootcamp.reportingservice.data;

import java.util.Objects;

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
        year = Objects.requireNonNull(year, "year must not be null").toUpperCase();
        month = Objects.requireNonNull(month, "month must not be null").toUpperCase();
    }
}
/*
{
  "trainerUsername": "",
  "trainerFirstName": "",
  "trainerLastName" : "",
  "active": true,
  "duration": 2,
  "year": "2024",
  "month": "JAN",
  "delete": false
}
 */
