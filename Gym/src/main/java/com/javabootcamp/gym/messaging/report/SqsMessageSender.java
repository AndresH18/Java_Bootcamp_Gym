package com.javabootcamp.gym.messaging.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabootcamp.gym.messaging.IMessage;
import com.javabootcamp.gym.messaging.IMessageSender;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.UUID;

public class SqsMessageSender implements IMessageSender {
    private static final String MESSAGE_GROUP_ID = "report";

    private final ObjectMapper mapper = new ObjectMapper();

    private final SqsClient client;
    private final String sqsEndpoint;

    public SqsMessageSender(SqsClient client, String sqsEndpoint) {
        this.client = client;
        this.sqsEndpoint = sqsEndpoint;
    }

    @Override
    public void sendMessage(String destination, IMessage message) throws JsonProcessingException {
        var body = mapper.writeValueAsString(message);
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(sqsEndpoint)
                .messageBody(body)
                .messageGroupId(MESSAGE_GROUP_ID)
                .messageDeduplicationId(UUID.randomUUID().toString())
                .build();

        client.sendMessage(request);
    }

//    public void sendBatch(String... messages) {
//        // implement batch
//    }
}
