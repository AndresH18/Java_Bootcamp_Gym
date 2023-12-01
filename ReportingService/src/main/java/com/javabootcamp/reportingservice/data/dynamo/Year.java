package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DynamoDbBean
public class Year {
    private String year;

    private List<Month> months;

    public Year(String year) {
        this();
        this.year = year;
    }

    public Year() {
        months = new ArrayList<>();
    }


    //    @DynamoDbPartitionKey
    @DynamoDbAttribute("year")
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Month> getMonths() {
        return months;
    }

    public void setMonths(List<Month> months) {
        this.months = months;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Year year1 = (Year) o;

        return Objects.equals(year, year1.year);
    }

    @Override
    public int hashCode() {
        return year != null ? year.hashCode() : 0;
    }
}
