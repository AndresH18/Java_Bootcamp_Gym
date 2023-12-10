package com.javabootcamp.gym.messaging.report;

public interface ISqsQueue {
    void sendMessage(String message);
}
