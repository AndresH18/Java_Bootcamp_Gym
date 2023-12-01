package com.javabootcamp.gym.messaging.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabootcamp.gym.messaging.TrainingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ReportingServiceAsync implements IReportingService<TrainingMessage>, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportingServiceAsync.class);
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_COOLDOWN = 1;

    private final ObjectMapper mapper = new ObjectMapper();
    private final QueueMessagingTemplate queueMessagingTemplate;

    private final BlockingQueue<TrainingMessage> queue;
    private final ExecutorService executor;
    private final AtomicBoolean running;

    @Autowired
    public ReportingServiceAsync(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.running = new AtomicBoolean(true);
        this.queue = new LinkedBlockingQueue<>();
        this.executor = Executors.newSingleThreadExecutor();

        // use this instead when many threads are needed to send multiple messages at a time
        // this.executor = Executors.newCachedThreadPool();

        startProcessing();
    }

    /**
     * Adds the {@link TrainingMessage} to be sent asynchronously.
     * <p>
     * This method returns immediately
     *
     * @param message The message to be sent
     * @return Returns {@code true} if the message was added to the queue, {@code false} otherwise
     */
    public boolean sendMessage(TrainingMessage message) {
        LOGGER.info("Queuing message");
        return queue.offer(message);
    }

    /**
     * Starts the processing of information in a separate thread.
     */
    private void startProcessing() {
        executor.execute(() -> {
            while (running.get()) {
                try {
                    TrainingMessage message = queue.take();
                    send(message);
                } catch (InterruptedException e) {
                    LOGGER.error("Thread interrupted while sending message", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * Sends the training message to the cloud Queue Service
     *
     * @param message The message to send
     */
    @SuppressWarnings("deprecated")
    private void send(TrainingMessage message) {
        int attempt = 1;
        boolean success = false;
        var transactionId = UUID.randomUUID().toString();
        do {
            LOGGER.info("Thread {}. Sending message. Transaction_ID={}", Thread.currentThread().getId(), transactionId);
            try {
                var json = mapper.writeValueAsString(message);
                var m = buildMessage(json);
                queueMessagingTemplate.send(m);
                success = true;
            } catch (Exception e) {
                LOGGER.error("Thread %d. Failed to send message. Attempt %d. Retrying in %d. Transaction_ID=%s"
                                .formatted(
                                        Thread.currentThread().getId(),
                                        attempt,
                                        RETRY_COOLDOWN,
                                        transactionId),
                        e);
                attempt++;

                try {
                    Thread.sleep(RETRY_COOLDOWN);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } while (attempt <= MAX_RETRIES && !success);
    }

    private Message<String> buildMessage(String messageString) {
        var builder = org.springframework.messaging.support.MessageBuilder.withPayload(messageString);
        return builder.build();
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
