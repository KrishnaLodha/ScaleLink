package com.scalink.repository;

import com.scalink.entity.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    Optional<Url> findByCustomAlias(String customAlias);

    boolean existsByShortCode(String shortCode);

    boolean existsByCustomAlias(String customAlias);

    @Query("SELECT u FROM Url u WHERE u.shortCode = :code OR u.customAlias = :code")
    Optional<Url> findByCodeOrAlias(@Param("code") String code);

    List<Url> findByUser_IdOrderByCreatedAtDesc(Long userId);

    long countByUser_Id(Long userId);

    @Query("SELECT COALESCE(SUM(u.clickCount), 0) FROM Url u WHERE u.user.id = :userId")
    long sumClickCountByUserId(@Param("userId") Long userId);

    List<Url> findTop5ByUser_IdOrderByClickCountDesc(Long userId);

    Page<Url> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT u FROM Url u ORDER BY u.clickCount DESC")
    List<Url> findTopPopular(Pageable pageable);
}
