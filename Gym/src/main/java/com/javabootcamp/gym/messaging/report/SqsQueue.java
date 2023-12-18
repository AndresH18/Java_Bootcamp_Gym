package com.javabootcamp.gym.messaging.report;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.UUID;

public class SqsQueue implements ISqsQueue {
    private static final String MESSAGE_GROUP_ID = "report";
    private final SqsClient client;
    private final String sqsEndpoint;
    private final String queueName;

    public SqsQueue(SqsClient client, String sqsEndpoint, String queueName) {
        this.client = client;
        this.sqsEndpoint = sqsEndpoint;
        this.queueName = queueName;
    }

    @Override
    public void sendMessage(String message) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(sqsEndpoint)
                .messageBody(message)
                .messageGroupId(MESSAGE_GROUP_ID)
                .messageDeduplicationId(UUID.randomUUID().toString())
                .build();

        client.sendMessage(request);
    }

//    public void sendBatch(String... messages) {
//        // implement batch
//    }
}
