package com.javabootcamp.gym.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IMessageSender {
    void sendMessage(String destination, IMessage message) throws JsonProcessingException;
}
