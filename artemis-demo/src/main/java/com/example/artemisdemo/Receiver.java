package com.example.artemisdemo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private static final String DESTINATION = "my-queue";

    @JmsListener(destination = DESTINATION)
    public void receive(String message) {
        System.out.println("message = " + message);
    }
}
