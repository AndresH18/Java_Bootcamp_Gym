package com.javabootcamp.gym.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!aws")
public class MessageSender implements IMessageSender {
    private final ObjectMapper mapper = new ObjectMapper();
    private final JmsTemplate jmsTemplate;

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendMessage(String destination, IMessage message) throws JsonProcessingException {
        var m = mapper.writeValueAsString(message);
        jmsTemplate.convertAndSend(destination, m);
    }
}
