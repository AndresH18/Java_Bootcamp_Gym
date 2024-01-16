package com.javabootcamp.reportingservice.data.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("trainers")
public class TrainingSummary {
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;
    private Map<String, Map<String, Integer>> summary;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, Map<String, Integer>> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, Map<String, Integer>> summary) {
        this.summary = summary;
    }
}
