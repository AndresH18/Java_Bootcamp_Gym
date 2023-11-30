package com.javabootcamp.reportingservice.messaging;

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

    @Autowired
    public SqsConsumer(TrainingSummaryService service) {
        this.service = service;
    }

    @SqsListener(value = "${cloud.aws.sqs.queue-name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void ReportTraining(TrainingMessage message) {
        if (message.delete()) {
            service.delete(message);
        } else {
            service.create(message);
        }

    }
}
