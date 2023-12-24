package com.javabootcamp.reportingservice.services;

import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.data.dynamo.TrainingSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This is not recommended for use until we can perform multiple operations on a single dynamodb item on a transaction
 */
@Deprecated
public class TransactionalDynamoDbStoreService {

    /**
     * Finds the first element in the collection that matches the given predicate.
     *
     * @param collection The collection to search.
     * @param predicate  The predicate used to test elements.
     * @param <T>        The type of elements in the collection.
     * @return An {@code Optional} containing the first matching element, or empty if no element is present.
     */
    public static <T> Optional<T> getFirst(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).findFirst();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DynamoDbTable<TrainingSummary> table;
    private final DynamoDbClient client;


    public TransactionalDynamoDbStoreService(DynamoDbTable<TrainingSummary> table, DynamoDbClient client) {
        this.table = table;
        this.client = client;
        createTableIfNotExists();
    }

    /**
     * This method show how to create a transaction that will create the different required attributes for an entity.
     * <b>However</b>, transactions do not support multiple operations on a single item, therefore this approach is invalid.
     *
     * <p>I'm leaving it here for now, because I'm to tired and don't want to continue with this.</p>
     * <p>{@link TransactionalDynamoDbStoreService#delete(TrainingMessage)} should do the same, but instead of adding at
     * the end the value, it should subtract it</p>
     */
    public void create(TrainingMessage message) {


        TransactWriteItemsRequest transaction =
                TransactWriteItemsRequest.builder()
                        .transactItems(
                                TransactWriteItem.builder()
                                        .update(Update.builder()
                                                .tableName(TrainingSummary.class.getSimpleName())
                                                .key(Map.of("trainerUsername", AttributeValue.builder().s(message.trainerUsername()).build()))
                                                .updateExpression("SET #s = if_not_exists(#s, :val)")
                                                .expressionAttributeNames(Map.of("#s", "summary"))
                                                .expressionAttributeValues(Map.of(":val", AttributeValue.builder().m(Collections.emptyMap()).build()))
                                                .build())
                                        .build(),
                                TransactWriteItem.builder()
                                        .update(Update.builder()
                                                .tableName(TrainingSummary.class.getSimpleName())
                                                .key(Map.of("trainerUsername", AttributeValue.builder().s(message.trainerUsername()).build()))
                                                .updateExpression("SET #s.#y = if_not_exists(#s.$y, :val)")
                                                .expressionAttributeNames(Map.of(
                                                        "#s", "summary",
                                                        "#y", message.year()
                                                ))
                                                .expressionAttributeValues(Map.of(":val", AttributeValue.builder().m(Collections.emptyMap()).build()))
                                                .build())
                                        .build(),
                                TransactWriteItem.builder()
                                        .update(Update.builder()
                                                .tableName(TrainingSummary.class.getSimpleName())
                                                .key(Map.of("trainerUsername", AttributeValue.builder().s(message.trainerUsername()).build()))
                                                // delete will use "-" instead
                                                .updateExpression("SET #s.#y.#m = if_not_exists(#s.#y.#m, :zero) + :val")
                                                .expressionAttributeNames(Map.of(
                                                        "#s", "summary",
                                                        "#y", message.year()
                                                ))
                                                .expressionAttributeValues(Map.of(
                                                        ":val", AttributeValue.builder().n(message.duration().toString()).build(),
                                                        ":zero", AttributeValue.builder().n("0").build()
                                                ))
                                                .build())
                                        .build()

                        ).build();

        client.transactWriteItems(transaction);

//        UpdateItemRequest request = UpdateItemRequest.builder()
//                .tableName(TrainingSummary.class.getSimpleName())
//                .key(Map.of("trainerUsername", AttributeValue.builder().s(message.trainerUsername()).build()))
//                .updateExpression("SET #s.#y.#m = :val")
//                .expressionAttributeNames(Map.of(
//                        "#s", "summary",
//                        "#y", "A2021",
//                        "#m", "JAN"
//                ))
//                .expressionAttributeValues(Map.of(
//                        ":val", AttributeValue.builder().n("123").build()
//                ))
//                .build();
//        client.updateItem(request);
    }


    public void delete(TrainingMessage message) {
//        var summary = getSummary(message.trainerUsername());
//        if (summary == null) return;
//
//        var trainings = getTrainings(summary, message.year(), message.month());
//        trainings.remove(message.duration());
//
//        table.updateItem(summary);
    }

    private void checkSchemaTransactionBuilder(String username, String year, String month) {

    }

    private void createTableIfNotExists() {
        if (!doesTableExist()) {
            try {
                table.createTable();
                // The 'dynamoDbClient' instance that's passed to the builder for the DynamoDbWaiter is the same instance
                // that was passed to the builder of the DynamoDbEnhancedClient instance used to create the 'customerDynamoDbTable'.
                // This means that the same Region that was configured on the standard 'dynamoDbClient' instance is used for all service clients.
                try (DynamoDbWaiter waiter = DynamoDbWaiter.builder().client(client).build()) { // DynamoDbWaiter is Autocloseable


                    ResponseOrException<DescribeTableResponse> response = waiter.waitUntilTableExists(builder -> builder.tableName(TrainingSummary.class.getSimpleName()).build()).matched();
                    DescribeTableResponse tableDescription = response.response().orElseThrow(() -> new RuntimeException("TrainingSummary table was not created."));
                    // The actual error can be inspected in response.exception()
                    logger.info("TrainingSummary table was created.");
                } catch (Exception e) {
                    logger.error("Error creating table", e);
                }
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
