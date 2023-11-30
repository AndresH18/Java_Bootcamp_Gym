package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

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
    //    @DynamoDbPartitionKey

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
}
