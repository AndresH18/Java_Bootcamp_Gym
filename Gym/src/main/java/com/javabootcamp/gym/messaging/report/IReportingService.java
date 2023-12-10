package com.javabootcamp.gym.messaging.report;

public interface IReportingService<T> {
    /**
     * Sends the content asynchronously
     * <p>
     * This method returns immediately
     *
     * @param t The object to be sent
     */
    void sendMessage(T t);
}
