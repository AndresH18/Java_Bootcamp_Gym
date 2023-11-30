package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Month month1 = (Month) o;

        if (!Objects.equals(month, month1.month)) return false;
        return Objects.equals(trainings, month1.trainings);
    }

    @Override
    public int hashCode() {
        int result = month != null ? month.hashCode() : 0;
        result = 31 * result + (trainings != null ? trainings.hashCode() : 0);
        return result;
    }
}
