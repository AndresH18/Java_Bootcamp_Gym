package com.javabootcamp.reportingservice.services;

import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.data.TrainingSummaryBuilder;
import com.javabootcamp.reportingservice.data.dynamo.Month;
import com.javabootcamp.reportingservice.data.dynamo.Training;
import com.javabootcamp.reportingservice.data.dynamo.TrainingSummary;
import com.javabootcamp.reportingservice.data.dynamo.Year;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class TrainingSummaryService implements IReportingService<TrainingMessage, TrainingSummary> {

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

    @Autowired
    public TrainingSummaryService(DynamoDbTable<TrainingSummary> table, DynamoDbClient client) {
        this.table = table;
        createTableIfNotExists(client);
    }

    @Override
    public void create(TrainingMessage message) {
        var summary = getSummary(message.trainerUsername());

        if (summary != null) {
            updateInformation(summary, message);
            var year = getYear(summary, message.year());

            if (year == null)
                year = addYear(summary, message.year());

            var month = getMonth(year, message.month());

            if (month == null)
                month = addMonth(year, message.month());

            month.getTrainings().add(new Training(message.duration()));

        } else {
            var builder = TrainingSummaryBuilder.builder()
                    .setUsername(message.trainerUsername())
                    .setFirstName(message.trainerFirstName())
                    .setLastName(message.trainerLastName())
                    .setTraining(new Training(message.duration()))
                    .setYear(message.year())
                    .setMonth(message.month())
                    .setActive(message.active());

            summary = builder.build();
        }
        // update if exists, create if doesnt exists
        table.updateItem(summary);

    }

    @Override
    public void delete(TrainingMessage message) {
        var summary = getSummary(message.trainerUsername());
        if (summary == null)
            return;

        var trainings = getTrainings(summary, message.year(), message.month());
        trainings.remove(new Training(message.duration()));

        table.updateItem(summary);
    }


    private void updateInformation(TrainingSummary summary, TrainingMessage message) {
        summary.setTrainerFirstName(message.trainerFirstName());
        summary.setTrainerLastName(message.trainerLastName());
        summary.setActive(message.active());
    }

    private TrainingSummary getSummary(String username) {
        try {
            TrainingSummary summary = table.getItem(Key.builder().partitionValue(username).build());
            return summary;
        } catch (Exception e) {
            return null;
        }
    }

    private Year getYear(TrainingSummary summary, String year) {
        return getFirst(summary.getYears(), y -> y.getYear().equalsIgnoreCase(year)).orElse(null);
    }

    private Month getMonth(Year year, String month) {
        return getFirst(year.getMonths(), m -> m.getMonth().equalsIgnoreCase(month)).orElse(null);
    }

    private List<Training> getTrainings(TrainingSummary summary, String year, String month) {
        var y = getYear(summary, year);
        var m = getMonth(y, month);
        return m.getTrainings();
    }

    private Year addYear(TrainingSummary summary, String year) {
        Year y = new Year(year);
        summary.getYears().add(y);
        return y;
    }

    private Month addMonth(Year year, String month) {
        Month m = new Month(month);
        year.getMonths().add(m);
        return m;
    }


    private void createTableIfNotExists(DynamoDbClient client) {
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
