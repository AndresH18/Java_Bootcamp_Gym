package com.javabootcamp.gym.configuration;

import com.javabootcamp.gym.messaging.report.EmptySqsQueue;
import com.javabootcamp.gym.messaging.report.ISqsQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.yaml")
public class Config {
    @Bean
    @Profile("!aws")
    public ISqsQueue noAwsQueue() {
        return new EmptySqsQueue();
    }

}
