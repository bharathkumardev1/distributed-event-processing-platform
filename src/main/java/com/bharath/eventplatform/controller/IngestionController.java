package com.bharath.eventplatform.controller;

import com.bharath.eventplatform.model.EventPayload;
import com.bharath.eventplatform.service.EventProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class IngestionController {

    private final EventProducerService producerService;

    public IngestionController(EventProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<Void> ingest(@RequestBody EventPayload payload) {
        producerService.sendEvent(payload);
        return ResponseEntity.accepted().build();
    }
}
