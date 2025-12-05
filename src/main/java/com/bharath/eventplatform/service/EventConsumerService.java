package com.bharath.eventplatform.service;

import com.bharath.eventplatform.model.EventPayload;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EventConsumerService {

    private static final Logger log = LoggerFactory.getLogger(EventConsumerService.class);

    private final IdempotencyService idempotencyService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String dlqTopic;

    public EventConsumerService(IdempotencyService idempotencyService,
                                KafkaTemplate<String, Object> kafkaTemplate,
                                @Value("${app.kafka.dlq-topic}") String dlqTopic) {
        this.idempotencyService = idempotencyService;
        this.kafkaTemplate = kafkaTemplate;
        this.dlqTopic = dlqTopic;
    }

    @KafkaListener(topics = "${app.kafka.events-topic}", groupId = "event-consumers")
    public void handleEvent(@Payload EventPayload payload) {
        String eventId = payload.getEventId();
        if (eventId == null || eventId.isBlank()) {
            log.warn("Received event without eventId, sending to DLQ: {}", payload);
            sendToDlq(payload, "MISSING_EVENT_ID");
            return;
        }

        boolean shouldProcess = idempotencyService.markIfNotProcessed(eventId);
        if (!shouldProcess) {
            log.info("Skipping duplicate event with id: {}", eventId);
            return;
        }

        try {
            // Simulated business logic. In real systems this could be payment processing, etc.
            log.info("Processing event: {}", payload);
            simulateProcessing(payload);
            log.info("Successfully processed eventId={}", eventId);
        } catch (Exception ex) {
            log.error("Failed to process eventId={}, routing to DLQ", eventId, ex);
            sendToDlq(payload, "PROCESSING_ERROR");
        }
    }

    private void simulateProcessing(EventPayload payload) throws Exception {
        // Here you can add dummy validations or simple branching to make it look real.
        if ("FAIL_ME".equalsIgnoreCase(payload.getType())) {
            throw new RuntimeException("Simulated processing failure");
        }
    }

    private void sendToDlq(EventPayload payload, String reason) {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(dlqTopic, payload.getEventId(), payload);
        record.headers().add("dlq-reason", reason.getBytes());
        kafkaTemplate.send(record);
    }
}
