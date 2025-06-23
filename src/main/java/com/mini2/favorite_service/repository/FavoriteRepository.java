package com.mini2.favorite_service.repository;

import java.util.List;
import java.util.Optional;

import com.mini2.favorite_service.domain.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface FavoriteRepository extends JpaRepository<Favorite, Long>{
    Page<Favorite> findByUserId(Long userId, Pageable pageable);

    Optional<Favorite> findByUserIdAndNewsLink(Long userId, String newsLink);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
