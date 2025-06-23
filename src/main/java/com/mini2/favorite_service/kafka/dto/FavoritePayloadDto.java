package com.mini2.favorite_service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePayloadDto {
    private String newsCategory;
    private String createdTime;
}