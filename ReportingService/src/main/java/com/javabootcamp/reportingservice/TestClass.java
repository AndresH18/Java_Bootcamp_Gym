package com.javabootcamp.reportingservice;

import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.messaging.SqsConsumer;
import com.javabootcamp.reportingservice.services.TrainingSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
 @RequestMapping("dev")
public class TestClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SqsConsumer consumer;
    private final TrainingSummaryService service;

    public TestClass(SqsConsumer consumer, TrainingSummaryService service) {
        this.consumer = consumer;
        this.service = service;
    }

    @GetMapping
    public void get() {
        logger.debug("dev: get");
        var m = new TrainingMessage(
                "andres.hoyos",
                "Andres",
                "Hoyos",
                true,
                10,
                "2023",
                "NOV",
                true);

        consumer.ReportTraining(m);
    }
}
