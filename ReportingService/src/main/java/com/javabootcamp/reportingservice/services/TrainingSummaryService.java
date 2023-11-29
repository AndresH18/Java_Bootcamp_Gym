package com.javabootcamp.reportingservice.services;

import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.data.dynamo.TrainingSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Service
public class TrainingSummaryService implements IReportingService<TrainingMessage, TrainingSummary> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DynamoDbTable<TrainingSummary> table;

    @Autowired
    public TrainingSummaryService(DynamoDbTable<TrainingSummary> table, DynamoDbClient client) {
        this.table = table;
        createTableIfNotExists(client);
    }

    @Override
    public void create(TrainingMessage trainingMessage) {

    }

    private void createTableIfNotExists(DynamoDbClient client) {
        if (!doesTableExist()) {
            table.createTable();

            // The 'dynamoDbClient' instance that's passed to the builder for the DynamoDbWaiter is the same instance
            // that was passed to the builder of the DynamoDbEnhancedClient instance used to create the 'customerDynamoDbTable'.
            // This means that the same Region that was configured on the standard 'dynamoDbClient' instance is used for all service clients.
            try (DynamoDbWaiter waiter = DynamoDbWaiter.builder().client(client).build()) { // DynamoDbWaiter is Autocloseable
                ResponseOrException<DescribeTableResponse> response = waiter
                        .waitUntilTableExists(builder -> builder.tableName(TrainingSummary.class.getSimpleName()).build())
                        .matched();
                DescribeTableResponse tableDescription = response.response().orElseThrow(
                        () -> new RuntimeException("TrainingSummary table was not created."));
                // The actual error can be inspected in response.exception()
                logger.info("TrainingSummary table was created.");
            } catch (Exception e) {
                logger.error("Error creating table", e);
            }
        }
    }

    private boolean doesTableExist() {
        try {
            table.describeTable();
            return true;
        } catch (ResourceNotFoundException e) {
            logger.error("DynamoDb resource not found", e);
            return false;
        }
    }
}
