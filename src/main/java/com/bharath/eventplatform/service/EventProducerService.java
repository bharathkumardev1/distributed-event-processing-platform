package com.bharath.eventplatform.service;

import com.bharath.eventplatform.model.EventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducerService {

    private static final Logger log = LoggerFactory.getLogger(EventProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String eventsTopic;

    public EventProducerService(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.events-topic}") String eventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventsTopic = eventsTopic;
    }

    public void sendEvent(EventPayload payload) {
        log.info("Producing event: {}", payload);
        kafkaTemplate.send(eventsTopic, payload.getEventId(), payload);
    }
}
