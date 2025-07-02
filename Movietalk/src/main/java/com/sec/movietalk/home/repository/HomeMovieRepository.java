package com.sec.movietalk.home.repository;

import com.sec.movietalk.common.domain.movie.MovieViews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMovieRepository extends JpaRepository<MovieViews, Long> {

    @Query("""
        SELECT mv.movieId AS movieId, COUNT(mv) AS cnt
        FROM MovieViews mv
        GROUP BY mv.movieId
        ORDER BY cnt DESC
        """)

    Page<MovieViewsCountProjection> findTop4ByMovieIdCount(Pageable pageable);

    interface MovieViewsCountProjection {
        Integer getMovieId();
        Long getCnt();
    }
}
