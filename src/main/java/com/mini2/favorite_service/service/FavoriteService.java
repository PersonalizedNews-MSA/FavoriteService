package com.mini2.favorite_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mini2.favorite_service.common.exception.CustomException;
import com.mini2.favorite_service.common.exception.ErrorCode;
import com.mini2.favorite_service.domain.Favorite;
import com.mini2.favorite_service.dto.request.FavoriteRequestDto;
import com.mini2.favorite_service.dto.response.FavoriteResponseDto;
import com.mini2.favorite_service.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Optional<FavoriteResponseDto> likeNews(Long userId, FavoriteRequestDto dto){
        Optional<Favorite> existing = favoriteRepository.findByUserIdAndNewsLink(userId, dto.getNewsLink());

        if(existing.isPresent()){
            favoriteRepository.delete(existing.get());
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
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

        if (!favorite.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        favoriteRepository.delete(favorite);
    }
}