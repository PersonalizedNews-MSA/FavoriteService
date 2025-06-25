package com.mini2.favorite_service.service;

import com.mini2.favorite_service.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final FavoriteRepository favoriteRepository;

    public List<String> getUserFavoriteLink(Long userId) {
        return favoriteRepository.findNewsLinksByUserId(userId);
    }
}
