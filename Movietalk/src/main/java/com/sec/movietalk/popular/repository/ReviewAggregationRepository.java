package com.sec.movietalk.popular.repository;

import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.popular.repository.projection.ReviewCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReviewAggregationRepository
        extends JpaRepository<Review, Long> {

    /**
     * 기간(from ~ now) 안에서 리뷰 수 TOP N 영화 집계
     */
    @Query("""
           SELECT  r.movieId        AS movieId,
                   COUNT(r)         AS cnt
             FROM  Review r
            WHERE  r.createdAt >= :from
            GROUP  BY r.movieId
            ORDER  BY cnt DESC
           """)
    Page<ReviewCountProjection> findTopMoviesByReviewCount(
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    /**
     * 전체 기간(ALLTIME) 리뷰 수 TOP N 영화 집계
     */
    @Query("""
           SELECT  r.movieId        AS movieId,
                   COUNT(r)         AS cnt
             FROM  Review r
            GROUP  BY r.movieId
            ORDER  BY cnt DESC
           """)
    Page<ReviewCountProjection> findTopMoviesByReviewCountAllTime(Pageable pageable);
}
