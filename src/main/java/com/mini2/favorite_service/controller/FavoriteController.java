package com.mini2.favorite_service.controller;


import com.mini2.favorite_service.common.web.context.GatewayRequestHeaderUtils;
import com.mini2.favorite_service.dto.request.FavoriteRequestDto;
import com.mini2.favorite_service.dto.response.FavoriteResponseDto;
import com.mini2.favorite_service.service.FavoriteService;
import com.mini2.favorite_service.service.NewsService;
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
    private final NewsService newsService;

    @PostMapping("/")
    public ResponseEntity<?> toggleFavorite(@RequestBody FavoriteRequestDto dto) {
        Long userId = Long.valueOf(GatewayRequestHeaderUtils.getUserIdOrThrowException());

        Optional<FavoriteResponseDto> result = favoriteService.likeNews(userId, dto);

        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<FavoriteResponseDto>> getMyFavorites() {
        Long userId = Long.valueOf(GatewayRequestHeaderUtils.getUserIdOrThrowException());

        List<FavoriteResponseDto> myFavorites = favoriteService.getMyFavorites(userId);
        return ResponseEntity.ok(myFavorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> cancelFavorite(@PathVariable Long favoriteId) {
        Long userId = Long.valueOf(GatewayRequestHeaderUtils.getUserIdOrThrowException());

        favoriteService.cancelLike(favoriteId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/newslinks/{userId}")
    public ResponseEntity<List<String>> favoriteNewsLinks(@PathVariable Long userId) {
        List<String> favoriteNewsLinkList = newsService.getUserFavoriteLink(userId);
        return ResponseEntity.ok(favoriteNewsLinkList);
    }
}
