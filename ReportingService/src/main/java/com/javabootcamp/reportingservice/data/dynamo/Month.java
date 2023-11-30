package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DynamoDbBean
public class Month {
    private String month;
    private List<Training> trainings;

    public Month(String month) {
        this();
        this.month = month;
    }

    public Month() {
        trainings = new ArrayList<>();
    }

    @DynamoDbPartitionKey
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Month month1 = (Month) o;

        return Objects.equals(month, month1.month);
    }

    @Override
    public int hashCode() {
        return month != null ? month.hashCode() : 0;
    }
}
