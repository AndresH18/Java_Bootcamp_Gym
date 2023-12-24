package com.example.artemisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ArtemisDemoApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =  SpringApplication.run(ArtemisDemoApplication.class, args);
        Sender sender = context.getBean(Sender.class);
        sender.send("HELLO WORLD");
    }
}