# Distributed Event Processing Platform

[![Java](https://img.shields.io/badge/Java-17-blue.svg)]()
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.3-success.svg)]()
[![Kafka](https://img.shields.io/badge/Kafka-Event%20Streaming-black.svg)]()
[![Redis](https://img.shields.io/badge/Redis-Caching-orange.svg)]()
[![Docker Compose](https://img.shields.io/badge/Docker-Compose-blue.svg)]()

A high-scale event ingestion and delivery platform simulating enterprise payments processing.

This project demonstrates:

- Event-driven microservices
- Idempotent processing with Redis
- Dead-letter queue (DLQ) handling
- Basic observability and metrics-first thinking

---

## Architecture

```mermaid
flowchart LR
A[Client] -->|REST /api/events| B(Ingestion Service)
B -->|Kafka Producer| C[events-topic]
C --> D[Consumer Service]
D -->|Success| E[(Redis: Processed IDs)]
D -->|Failure| F[dlq-topic]


Tech Stack
Language-	Java 17
Framework- Spring Boot
Messaging- Apache Kafka
Caching- Redis
Containers- Docker Compose
Testing- JUnit, Mockito

Features
REST API for event ingestion (/api/events)
Kafka producer for event publishing
Consumer with Redis-based idempotency (no duplicate processing)
Dead-letter queue pattern for failed events
Clean structure suitable for extension

Getting Started

Prerequisites
JDK 17+
Maven 3.9+
Docker + Docker Compose
Kafka & Redis

Example Event Payload
{
  "eventId": "e123",
  "type": "PAYMENT_CREATED",
  "payload": {
    "amount": 100.0,
    "currency": "USD",
    "userId": "user-001"
  }
}

High-Level Flow

Client calls /api/events with JSON payload.
Service publishes event to Kafka topic (events-topic).
Consumer reads message, checks Redis for eventId.
If not processed, it applies business logic and marks as processed.
If processing fails, message is sent to DLQ topic.

Roadmap

 Add proper DLQ consumer and replay endpoint
 Add Prometheus metrics and Grafana dashboard
 Add integration tests for idempotency behavior

Author

Bharath Kumar Mandha
LinkedIn: linkedin.com/in/bharathkumar3a


