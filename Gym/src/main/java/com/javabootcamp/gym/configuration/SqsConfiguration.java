package com.javabootcamp.gym.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessageChannel;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqsConfiguration {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.sqs.endpoint}")
    private String endpoint;

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        var template = new QueueMessagingTemplate(amazonSQSAsync);
        template.setDefaultDestinationName(queueName);
        return template;
    }

    @Bean
    public AmazonSQSAsync amazonSQS() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTPS); // Protocol can be HTTP or HTTPS

        return AmazonSQSAsyncClientBuilder.standard()
                .withClientConfiguration(clientConfiguration)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
    }
}
