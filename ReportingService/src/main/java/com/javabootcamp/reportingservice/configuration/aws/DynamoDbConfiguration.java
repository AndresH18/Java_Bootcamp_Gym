package com.javabootcamp.reportingservice.configuration.aws;


import com.javabootcamp.reportingservice.data.dynamo.TrainingSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@Profile("aws")
public class DynamoDbConfiguration {

    //    @Value("${cloud.aws.sqs.endpoint}")
//    private String endpoint;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public DynamoDbTable<TrainingSummary> trainingSummaryDynamoDbTable(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return dynamoDbEnhancedClient
                .table(TrainingSummary.class.getSimpleName(),
                        TableSchema.fromBean(TrainingSummary.class));
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient).build();
        return enhancedClient;
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(region))
//                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        return dynamoDbClient;
    }
}
