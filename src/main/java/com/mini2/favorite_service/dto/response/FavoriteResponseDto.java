package com.mini2.favorite_service.dto.response;

import com.mini2.favorite_service.domain.Favorite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class FavoriteResponseDto {
    private Long id;
    private Long userId;
    private String newsTitle;
    private String newsSummary;
    private String newsLink;
    private String newsThumbnail;
    private String newsCategory;
    private LocalDateTime createdTime;

    public static FavoriteResponseDto from(Favorite fav) {
        return FavoriteResponseDto.builder()
                .id(fav.getId())
                .userId(fav.getUserId())
                .newsTitle(fav.getNewsTitle())
                .newsSummary(fav.getNewsSummary())
                .newsLink(fav.getNewsLink())
                .newsThumbnail(fav.getNewsThumbnail())
                .newsCategory(fav.getNewsCategory())
                .createdTime(fav.getCreatedTime())
                .build();
    }
}