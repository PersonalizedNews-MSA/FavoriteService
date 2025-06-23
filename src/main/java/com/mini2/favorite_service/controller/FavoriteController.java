package com.mini2.favorite_service.controller;

import com.mini2.favorite_service.dto.request.FavoriteRequestDto;
import com.mini2.favorite_service.dto.response.FavoriteResponseDto;
import com.mini2.favorite_service.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorite/v1")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<?> toggleFavorite(
            @RequestHeader("user_id") Long userId,
            @RequestBody FavoriteRequestDto dto) {

        Optional<FavoriteResponseDto> result = favoriteService.likeNews(userId, dto);

        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponseDto>> getMyFavorites(
            @RequestHeader("user_id") Long userId) {

        List<FavoriteResponseDto> myFavorites = favoriteService.getMyFavorites(userId);
        return ResponseEntity.ok(myFavorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> cancelFavorite(
            @PathVariable Long favoriteId,
            @RequestHeader("user_id") Long userId) {

        favoriteService.cancelLike(favoriteId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}