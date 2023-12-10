package com.javabootcamp.gym.messaging.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptySqsQueue implements ISqsQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmptySqsQueue.class);

    public EmptySqsQueue() {
        LOGGER.info("Created instance of empty sqs queue. Will not send messages");
    }

    @Override
    public void sendMessage(String message) {
        LOGGER.info("Send message called");
    }
}