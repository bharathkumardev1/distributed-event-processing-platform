package com.bharath.eventplatform.controller;

import com.bharath.eventplatform.model.EventPayload;
import com.bharath.eventplatform.service.EventProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class IngestionController {

    private static final Logger log = LoggerFactory.getLogger(IngestionController.class);

    private final EventProducerService producerService;

    public IngestionController(EventProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<Void> ingest(@RequestBody EventPayload payload) {
        log.info("Received ingestion request: {}", payload);
        producerService.sendEvent(payload);
        return ResponseEntity.accepted().build();
    }
}
