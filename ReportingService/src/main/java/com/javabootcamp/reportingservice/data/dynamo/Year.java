package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

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

        if (!Objects.equals(year, year1.year)) return false;
        return Objects.equals(months, year1.months);
    }

    @Override
    public int hashCode() {
        int result = year != null ? year.hashCode() : 0;
        result = 31 * result + (months != null ? months.hashCode() : 0);
        return result;
    }
}
