package com.mini2.favorite_service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteRequestDto {
    private String newsTitle;
    private String newsSummary;
    private String newsLink;
    private String newsThumbnail;
    private String newsCategory;
}