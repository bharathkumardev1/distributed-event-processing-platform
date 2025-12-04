package com.bharath.eventplatform.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.events-topic}")
    private String eventsTopic;

    @Value("${app.kafka.dlq-topic}")
    private String dlqTopic;

    @Bean
    public NewTopic eventsTopic() {
        return TopicBuilder.name(eventsTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name(dlqTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }
}
