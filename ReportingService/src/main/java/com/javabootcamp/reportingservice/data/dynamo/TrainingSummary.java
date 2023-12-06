package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.HashMap;
import java.util.Map;

@DynamoDbBean
public class TrainingSummary {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean active;

    private Map<String, Map<String, Integer>> summary;

    public TrainingSummary() {
//        summary = new HashMap<>();
//        summary.put("Hola", new HashMap<>(Map.of("2023", 10)));
    }

    public TrainingSummary(String trainerUsername, String trainerFirstName, String trainerLastName, boolean active) {
        this.trainerUsername = trainerUsername;
        this.trainerFirstName = trainerFirstName;
        this.trainerLastName = trainerLastName;
        this.active = active;
    }

    @DynamoDbPartitionKey
    public String getTrainerUsername() {
        return trainerUsername;
    }

//    @DynamoDbAttribute("summary")
    public Map<String, Map<String, Integer>> getSummary() {
        return summary;
    }

    public String getTrainerFirstName() {
        return trainerFirstName;
    }

    public String getTrainerLastName() {
        return trainerLastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public void setTrainerFirstName(String trainerFirstName) {
        this.trainerFirstName = trainerFirstName;
    }

    public void setTrainerLastName(String trainerLastName) {
        this.trainerLastName = trainerLastName;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setSummary(Map<String, Map<String, Integer>> summary) {
        this.summary = summary;
    }
}