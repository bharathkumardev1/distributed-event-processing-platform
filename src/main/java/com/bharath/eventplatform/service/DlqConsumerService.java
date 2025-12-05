package com.bharath.eventplatform.service;

import com.bharath.eventplatform.model.EventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DlqConsumerService {

    private static final Logger log = LoggerFactory.getLogger(DlqConsumerService.class);

    @KafkaListener(topics = "${app.kafka.dlq-topic}", groupId = "event-dlq-consumers")
    public void handleDlqEvent(@Payload EventPayload payload,
                               @Headers Map<String, Object> headers) {
        Object reason = headers.get("kafka_receivedMessageKey");
        log.error("DLQ event received: payload={}, headers={}", payload, headers);
    }
}
