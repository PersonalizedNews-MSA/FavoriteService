package com.mini2.favorite_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorite")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "news_title", nullable = false)
    private String newsTitle;

    @Column(name = "news_summary", columnDefinition = "TEXT")
    private String newsSummary;

    @Column(name = "news_link", nullable = false)
    private String newsLink;

    @Column(name = "news_thumbnail")
    private String newsThumbnail;

    @Column(name = "news_category")
    private String newsCategory;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;
}
