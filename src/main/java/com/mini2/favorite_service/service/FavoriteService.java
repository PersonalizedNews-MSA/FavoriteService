package com.mini2.favorite_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mini2.favorite_service.common.exception.NotFound;
import com.mini2.favorite_service.domain.Favorite;
import com.mini2.favorite_service.dto.request.FavoriteRequestDto;
import com.mini2.favorite_service.dto.response.FavoriteResponseDto;
import com.mini2.favorite_service.repository.FavoriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

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
        return Optional.of(FavoriteResponseDto.from(saved));
    }


    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getMyFavorites(Long userId){
        return favoriteRepository.findByUserId(userId).stream()
                .map(FavoriteResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelLike(Long favoriteId, Long userId){
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
    }
}