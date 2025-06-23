package com.mini2.favorite_service.service;

import com.mini2.favorite_service.domain.Favorite;
import com.mini2.favorite_service.dto.request.FavoriteRequestDto;
import com.mini2.favorite_service.dto.response.FavoriteResponseDto;
import com.mini2.favorite_service.kafka.KafkaMessageProducer;
import com.mini2.favorite_service.kafka.dto.FavoriteEventDto;
import com.mini2.favorite_service.kafka.dto.FavoritePayloadDto;
import com.mini2.favorite_service.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final KafkaMessageProducer kafkaProducer;

    @Transactional
    public Optional<FavoriteResponseDto> likeNews(Long userId, FavoriteRequestDto dto) {
        Optional<Favorite> existing = favoriteRepository.findByUserIdAndNewsLink(userId, dto.getNewsLink());

        if (existing.isPresent()) {
            log.warn("이미 등록된 뉴스입니다. userId={}, newsLink={}", userId, dto.getNewsLink());
            return Optional.empty();
        }

        Favorite favorite = Favorite.builder()
                .userId(userId)
                .newsTitle(dto.getNewsTitle())
                .newsSummary(dto.getNewsSummary())
                .newsLink(dto.getNewsLink())
                .newsThumbnail(dto.getNewsThumbnail())
                .newsCategory(dto.getNewsCategory())
                .createdTime(LocalDateTime.now())
                .build();

        Favorite saved = favoriteRepository.save(favorite);
        sendKafkaEvent("좋아요 등록", saved);

        return Optional.of(FavoriteResponseDto.from(saved));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getMyFavorites(Long userId, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return favoriteRepository.findByUserId(userId, pageable).stream()
                .map(FavoriteResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelLike(Long favoriteId, Long userId) {
        Optional<Favorite> favoriteOptional = favoriteRepository.findById(favoriteId);

        if (favoriteOptional.isEmpty()) {
            log.warn("좋아요 취소 실패 - 이미 삭제된 favorite입니다. favoriteId: {}, userId: {}", favoriteId, userId);
            return;
        }

        Favorite favorite = favoriteOptional.get();
        if (!favorite.getUserId().equals(userId)) {
            log.warn("좋아요 취소 실패 - 사용자 불일치. 요청 userId: {}, 실제 userId: {}", userId, favorite.getUserId());
            return;
        }

        favoriteRepository.delete(favorite);
        log.info("좋아요 취소 완료. favoriteId: {}, userId: {}", favoriteId, userId);

        sendKafkaEvent("좋아요 취소", favorite);
    }

    private void sendKafkaEvent(String eventId, Favorite favorite) {
        FavoritePayloadDto payload = new FavoritePayloadDto(
                favorite.getNewsCategory(),
                favorite.getCreatedTime(),
                favorite.getUserId().toString(),
                favorite.getId().toString()
        );

        FavoriteEventDto event = new FavoriteEventDto(
                eventId,
                LocalDateTime.now(),
                "favorite-service",
                payload
        );

        kafkaProducer.send("UserFavoriteInfo", event);
    }
}