package com.javabootcamp.reportingservice.data;

import com.javabootcamp.reportingservice.data.dynamo.Month;
import com.javabootcamp.reportingservice.data.dynamo.Training;
import com.javabootcamp.reportingservice.data.dynamo.TrainingSummary;
import com.javabootcamp.reportingservice.data.dynamo.Year;

import java.util.ArrayList;
import java.util.Arrays;

public class TrainingSummaryBuilder {
    public static TrainingSummaryBuilder builder() {
        return new TrainingSummaryBuilder();
    }

    private final TrainingSummary summary;
    private Training training;
    private String yearString;
    private String monthString;

    private TrainingSummaryBuilder() {
        summary = new TrainingSummary();
    }

    public TrainingSummaryBuilder setUsername(String username) {
        summary.setTrainerUsername(username);
        return this;
    }

    public TrainingSummaryBuilder setFirstName(String firstName) {
        summary.setTrainerFirstName(firstName);
        return this;
    }

    public TrainingSummaryBuilder setLastName(String lastName) {
        summary.setTrainerLastName(lastName);
        return this;
    }

    public TrainingSummaryBuilder setActive(boolean active) {
        summary.setActive(active);
        return this;
    }

    public TrainingSummaryBuilder setYear(String year) {
        this.yearString = year;
        return this;
    }

    public TrainingSummaryBuilder setMonth(String month) {
        this.monthString = month;
        return this;
    }

    public TrainingSummaryBuilder setTraining(Training training) {
        this.training = training;
        return this;
    }

    public TrainingSummary build() {
        var month = new Month(monthString);
        month.getTrainings().add(training);

        var year = new Year(yearString);
        year.getMonths().add(month);

        summary.getYears().add(year);

        return summary;
    }
}
