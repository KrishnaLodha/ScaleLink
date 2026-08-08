package com.scalink.repository;

import com.scalink.entity.Analytics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    long countByUrl_Id(Long urlId);

    @Query("SELECT COUNT(a) FROM Analytics a WHERE a.url.id = :urlId AND a.timestamp >= :since")
    long countByUrlIdSince(@Param("urlId") Long urlId, @Param("since") Instant since);

    @Query("""
            SELECT a.country, COUNT(a) FROM Analytics a
            WHERE a.url.id = :urlId AND a.country IS NOT NULL
            GROUP BY a.country ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findTopCountries(@Param("urlId") Long urlId, Pageable pageable);

    @Query("""
            SELECT a.browser, COUNT(a) FROM Analytics a
            WHERE a.url.id = :urlId AND a.browser IS NOT NULL
            GROUP BY a.browser ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findTopBrowsers(@Param("urlId") Long urlId, Pageable pageable);

    @Query("""
            SELECT a.device, COUNT(a) FROM Analytics a
            WHERE a.url.id = :urlId AND a.device IS NOT NULL
            GROUP BY a.device ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findTopDevices(@Param("urlId") Long urlId, Pageable pageable);

    @Query("""
            SELECT COALESCE(a.referrer, 'Direct'), COUNT(a) FROM Analytics a
            WHERE a.url.id = :urlId
            GROUP BY COALESCE(a.referrer, 'Direct') ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findTopReferrers(@Param("urlId") Long urlId, Pageable pageable);

    @Query(value = """
            SELECT CAST(timestamp AS DATE) AS click_date, COUNT(*) AS clicks
            FROM analytics
            WHERE url_id = :urlId
            GROUP BY CAST(timestamp AS DATE)
            ORDER BY click_date DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> findDailyClicks(@Param("urlId") Long urlId, @Param("limit") int limit);

    @Query("""
            SELECT a.country, COUNT(a) FROM Analytics a
            JOIN a.url u WHERE u.user.id = :userId AND a.country IS NOT NULL
            GROUP BY a.country ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findTopCountriesByUser(@Param("userId") Long userId, Pageable pageable);
}
