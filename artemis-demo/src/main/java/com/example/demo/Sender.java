package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    private static final String DESTINATION = "my-queue";
    private final JmsTemplate jmsTemplate;

    @Autowired
    public Sender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String message) {
        jmsTemplate.convertAndSend(DESTINATION, message);
    }
}
