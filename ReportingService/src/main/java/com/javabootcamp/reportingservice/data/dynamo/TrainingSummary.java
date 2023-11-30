package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
public class TrainingSummary {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean active;
    private List<Year> years;

    public TrainingSummary(String trainerUsername, String trainerFirstName, String trainerLastName, boolean active) {
        this();
        this.trainerUsername = trainerUsername;
        this.trainerFirstName = trainerFirstName;
        this.trainerLastName = trainerLastName;
        this.active = active;
    }

    public TrainingSummary() {
        years = new ArrayList<>();
    }

    @DynamoDbPartitionKey
    public String getTrainerUsername() {
        return trainerUsername;
    }

    public TrainingSummary setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
        return this;
    }

    public String getTrainerFirstName() {
        return trainerFirstName;
    }

    public void setTrainerFirstName(String trainerFirstName) {
        this.trainerFirstName = trainerFirstName;
    }

    public String getTrainerLastName() {
        return trainerLastName;
    }

    public void setTrainerLastName(String trainerLastName) {
        this.trainerLastName = trainerLastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Year> getYears() {
        return years;
    }

    public TrainingSummary setYears(List<Year> years) {
        this.years = years;
        return this;
    }
}
