package com.javabootcamp.reportingservice.services;

import com.javabootcamp.reportingservice.data.TrainingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StoreService implements IStoreService<TrainingMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

    @Override
    public boolean store(TrainingMessage trainingMessage) {
        LOGGER.info("Store message: {}", trainingMessage);
        return true;
    }
}
