package com.mini2.favorite_service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePayloadDto {
    private String newsCategory;
    private LocalDateTime createdTime;
    private String newsId;
    private String userId;
}