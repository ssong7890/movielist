package com.sec.movietalk.popular.repository;

import com.sec.movietalk.common.domain.movie.MovieViews;
import com.sec.movietalk.popular.repository.projection.ViewCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MovieViewAggregationRepository
        extends JpaRepository<MovieViews, Long> {

    /**
     * 기간(from ~ now) 안에서 상세 페이지 조회 수 TOP N 영화 집계
     */
    @Query("""
           SELECT  mv.movieId       AS movieId,
                   COUNT(mv)        AS cnt
             FROM  MovieViews mv
            WHERE  mv.viewedAt >= :from
            GROUP  BY mv.movieId
            ORDER  BY cnt DESC
           """)
    Page<ViewCountProjection> findTopMoviesByViewCount(
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    /**
     * 전체 기간(ALLTIME) 조회 수 TOP N 영화 집계
     */
    @Query("""
           SELECT  mv.movieId       AS movieId,
                   COUNT(mv)        AS cnt
             FROM  MovieViews mv
            GROUP  BY mv.movieId
            ORDER  BY cnt DESC
           """)
    Page<ViewCountProjection> findTopMoviesByViewCountAllTime(Pageable pageable);
}
