package com.mini2.favorite_service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteEventDto {
    private String eventId;
    private LocalDateTime timestamp;
    private String sourceService;
    private FavoritePayloadDto payload;
}