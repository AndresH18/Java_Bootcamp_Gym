package com.javabootcamp.reportingservice.data.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.HashMap;
import java.util.Map;

@DynamoDbBean
@Deprecated
public class SummaryItem {
    private Map<String, Integer> monthSummary;

    public SummaryItem() {
        monthSummary = new HashMap<>();
    }

    public Map<String, Integer> getMonthSummary() {
        return monthSummary;
    }

    public void setMonthSummary(Map<String, Integer> monthSummary) {
        this.monthSummary = monthSummary;
    }
}
