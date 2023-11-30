package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Training {
    private int duration;

    public Training(int duration) {
        this.duration = duration;
    }

    public Training() {
    }

    @DynamoDbPartitionKey
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Training training = (Training) o;

        return duration == training.duration;
    }

    @Override
    public int hashCode() {
        return duration;
    }
}
