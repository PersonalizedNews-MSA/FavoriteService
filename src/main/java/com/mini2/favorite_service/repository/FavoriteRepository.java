package com.mini2.favorite_service.repository;

import java.util.List;
import java.util.Optional;

import com.mini2.favorite_service.domain.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface FavoriteRepository extends JpaRepository<Favorite, Long>{
    List<Favorite> findByUserId(Long userId);;

    Optional<Favorite> findByUserIdAndNewsLink(Long userId, String newsLink);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    @Query("SELECT f.newsLink FROM Favorite f WHERE f.userId = :userId")
    List<String> findNewsLinksByUserId(@Param("userId") Long userId);
}
