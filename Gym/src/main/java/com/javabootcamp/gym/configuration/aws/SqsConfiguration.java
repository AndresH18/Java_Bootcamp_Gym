package com.javabootcamp.gym.configuration.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.javabootcamp.gym.messaging.IMessageSender;
import com.javabootcamp.gym.messaging.report.SqsMessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Profile("aws")
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
    @Profile("aws")
    public IMessageSender awsQueue(SqsClient client) {
        var queue = new SqsMessageSender(client, endpoint);
        return queue;
    }


    @Bean
    @Profile("aws")
    public SqsClient sqsClient() {
        SqsClient client = SqsClient.builder()
                .region(Region.of(region))
//                .credentialsProvider(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        return client;
    }

    @Bean
    @Profile("aws")
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        var template = new QueueMessagingTemplate(amazonSQSAsync);
        template.setDefaultDestinationName(queueName);
        return template;
    }

    @Bean
    @Profile("aws")
    public AmazonSQSAsync amazonSQS() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTPS); // Protocol can be HTTP or HTTPS

        return AmazonSQSAsyncClientBuilder.standard()
                .withClientConfiguration(clientConfiguration)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
    }
}
