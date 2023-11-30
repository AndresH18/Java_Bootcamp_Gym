package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DynamoDbBean
public class TrainingSummary {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean active;
    private List<Year> years;

    public TrainingSummary() {
        years = new ArrayList<>();
    }

    @DynamoDbPartitionKey
    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
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

    public void setYears(List<Year> years) {
        this.years = years;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainingSummary summary = (TrainingSummary) o;

        if (active != summary.active) return false;
        if (!Objects.equals(trainerUsername, summary.trainerUsername))
            return false;
        if (!Objects.equals(trainerFirstName, summary.trainerFirstName))
            return false;
        return Objects.equals(trainerLastName, summary.trainerLastName);
    }

    @Override
    public int hashCode() {
        int result = trainerUsername != null ? trainerUsername.hashCode() : 0;
        result = 31 * result + (trainerFirstName != null ? trainerFirstName.hashCode() : 0);
        result = 31 * result + (trainerLastName != null ? trainerLastName.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
