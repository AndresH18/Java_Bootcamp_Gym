package com.javabootcamp.reportingservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabootcamp.reportingservice.configuration.prometheus.ConsumerMetrics;
import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.services.TrainingSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TrainingSummaryService service;
    private final ConsumerMetrics metrics;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public SqsConsumer(TrainingSummaryService service, ConsumerMetrics metrics) {
        this.service = service;
        this.metrics = metrics;
    }

    @SqsListener(value = "gym-reporting-service-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void ReportTraining(String message) {
        logger.trace("Received sqs message. message: {}", message);
        try {
            var trainingMessage = objectMapper.readValue(message, TrainingMessage.class);

            if (trainingMessage.delete()) {
                service.delete(trainingMessage);
            } else {
                service.create(trainingMessage);
            }
            metrics.incrementMessagesReceived();
        } catch (JsonProcessingException exception) {
            logger.error("Couldn't parse json to TrainingMessage", exception);
            throw new RuntimeException("Error parsing json", exception);
        }
    }
}
