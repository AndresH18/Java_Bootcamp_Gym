package com.javabootcamp.reportingservice.controller.devonly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.messaging.IMessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("dev")
@Profile("dev & aws")
public class AwsDevController {
    private static final String[] YEARS = new String[]{"2019", "2020", "2021", "2022", "2023"};
    private static final String[] MONTHS = new String[]{"JAN", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};

    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IMessageConsumer<String> consumer;

    public AwsDevController(IMessageConsumer<String> consumer) {
        this.consumer = consumer;
    }

    @GetMapping("sqs")
    public void get() throws Exception {
        logger.debug("dev: get");
        var m = new TrainingMessage(
                "david.hoyos",
                "Andres",
                "Hoyos",
                true,
                random.nextInt(10) + 1,
//                YEARS[random.nextInt(YEARS.length)],
//                MONTHS[random.nextInt(MONTHS.length)],
                "2020",
                "JAN",
                false);

        var json = mapper.writeValueAsString(m);

        consumer.consumeMessage(json);
    }
}
