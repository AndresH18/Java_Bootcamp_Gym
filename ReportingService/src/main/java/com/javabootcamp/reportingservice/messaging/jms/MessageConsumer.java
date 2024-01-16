package com.javabootcamp.reportingservice.messaging.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabootcamp.reportingservice.configuration.prometheus.ConsumerMetrics;
import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.messaging.IMessageConsumer;
import com.javabootcamp.reportingservice.services.IStoreService;
import jakarta.jms.JMSException;
import jakarta.jms.MessageFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!aws")
public class MessageConsumer implements IMessageConsumer<String> {
    private static final String DESTINATION = "reports-queue";
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConsumerMetrics metrics;
    private final IStoreService<TrainingMessage> storeService;

    @Autowired
    public MessageConsumer(ConsumerMetrics metrics, IStoreService<TrainingMessage> storeService) {
        this.metrics = metrics;
        this.storeService = storeService;
    }

    @Override
    @JmsListener(destination = DESTINATION)
    public void consumeMessage(String message) throws JMSException {
        LOGGER.info("Received Messages: {}", message);
        metrics.incrementMessagesReceived();
        try {
            var trainingMessage = objectMapper.readValue(message, TrainingMessage.class);
            LOGGER.info("Parsed Message: {}", trainingMessage.toString());

            if (!storeService.store(trainingMessage)) {
                LOGGER.warn("Could not store message");
                throw new JMSException("Could not store message");
            }

        } catch (JsonProcessingException | RuntimeException e) {
            LOGGER.error("Failed to parse message", e);
            throw new MessageFormatException("Error parsing json");
        }
    }
}
