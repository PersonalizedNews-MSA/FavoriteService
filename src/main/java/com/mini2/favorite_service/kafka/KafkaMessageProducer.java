package com.mini2.favorite_service.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mini2.favorite_service.kafka.dto.FavoriteEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaMessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void send(String topic, FavoriteEventDto eventDto) {
        try {
            String message = objectMapper.writeValueAsString(eventDto);
            kafkaTemplate.send(topic, message);
            System.out.println("Kafka 메시지 전송됨: " + message);
        } catch (JsonProcessingException e) {
            System.err.println("Kafka 메시지 직렬화 실패: " + e.getMessage());
        }
    }
}