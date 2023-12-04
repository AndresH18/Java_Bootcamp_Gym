package com.javabootcamp.reportingservice.services;

import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.data.dynamo.TrainingSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Service
public class DynamoStoreService implements IStoreService<TrainingMessage>, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoStoreService.class);


    private final DynamoDbTable<TrainingSummary> table;
    private final BlockingQueue<TrainingMessage> queue;
    private final ExecutorService executor;
    private final AtomicBoolean running;

    @Autowired
    public DynamoStoreService(DynamoDbTable<TrainingSummary> table, DynamoDbClient client) {
        this.table = table;
        this.queue = new LinkedBlockingQueue<>();
        this.running = new AtomicBoolean(true);
        this.executor = Executors.newSingleThreadExecutor();

        createTableIfNotExists(client);
        startProcessing();
    }


    private void create(TrainingMessage message) {
        var training = updateTraining(message, message::duration);

        table.updateItem(training);

    }


    private void delete(TrainingMessage message) {
        var training = updateTraining(message, () -> -(message.duration()));

        table.updateItem(training);
    }

    /**
     * Updates the training summary based on the provided message and supplier.
     *
     * @param message  The TrainingMessage containing information for the update.
     * @param supplier A supplier providing an Integer value used for the update.
     * @return The updated {@link TrainingSummary}after the modifications.
     */
    private TrainingSummary updateTraining(TrainingMessage message, Supplier<Integer> supplier) {
        var training = getTrainingSummary(message.trainerUsername());
        if (training == null) {
            training = new TrainingSummary(message.trainerUsername(), message.trainerFirstName(), message.trainerLastName(), message.active());
        }

        var summary = training.getSummary();
        if (summary == null) {
            summary = new HashMap<>();
            training.setSummary(summary);
        }

        var months = summary.computeIfAbsent(message.year(), k -> new HashMap<>());
//        var months = summary.get(message.year());
//        if (months == null) {
//            months = new HashMap<>();
//            summary.put(message.year(), months);
//        }

        var monthValue = months.get(message.month());
        monthValue = supplier.get() + (monthValue != null ? monthValue : 0);
        months.put(message.month(), monthValue);
        return training;
    }

    private TrainingSummary getTrainingSummary(String username) {
        return table.getItem(Key.builder().partitionValue(username).build());
    }


    /**
     * Adds the {@link TrainingMessage} to be sent asynchronously.
     * <p>
     * This method returns immediately
     *
     * @param message The message to be sent
     * @return Returns {@code true} if the message was added to the queue, {@code false} otherwise
     */
    public boolean send(TrainingMessage message) {
        LOGGER.info("Queuing message");
        return queue.offer(message);
    }

    /**
     * Starts the processing of information in a separate thread.
     */
    public void startProcessing() {
        executor.execute(this::process);
    }

    private void process() {
        while (running.get()) {
            try {
                TrainingMessage message = queue.take();
                LOGGER.debug("Thread {}. Processing message", Thread.currentThread().getId());
                if (message.delete()) {
                    delete(message);
                } else {
                    create(message);
                }
            } catch (InterruptedException e) {
                LOGGER.error("Thread interrupted while sending message", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LOGGER.error("Error processing message", e);
            }
        }
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
                    /*DescribeTableResponse tableDescription = */
                    response.response().orElseThrow(() -> new RuntimeException("TrainingSummary table was not created."));
                    // The actual error can be inspected in response.exception()
                    LOGGER.info("TrainingSummary table was created.");
                } catch (Exception e) {
                    LOGGER.error("Error creating table", e);
                    throw e;
                }
            } catch (Exception e) {
                LOGGER.error("Error creating table", e);
                throw e;
            }
        }
    }

    private boolean doesTableExist() {
        try {
            table.describeTable();
            return true;
        } catch (ResourceNotFoundException e) {
            LOGGER.error("DynamoDb resource not found", e);
            return false;
        }
    }

    /**
     * Stops the processing loop and shuts down the executor service.
     */
    private void shutdown() throws SecurityException {
        running.set(false);
        executor.shutdown();
    }

    /**
     * Invoked by the containing {@code BeanFactory} on destruction of a bean.
     *
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     *                   but not rethrown to allow other beans to release their resources as well.
     */
    @SuppressWarnings("RedundantThrows")
    @Override
    public void destroy() throws Exception {
        shutdown();
    }

}
