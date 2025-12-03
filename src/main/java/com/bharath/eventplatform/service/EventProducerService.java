package com.bharath.eventplatform.service;

import com.bharath.eventplatform.model.EventPayload;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(EventPayload payload) {
        kafkaTemplate.send("events-topic", payload.getEventId(), payload);
    }
}
