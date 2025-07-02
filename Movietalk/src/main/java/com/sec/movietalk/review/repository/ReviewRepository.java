package com.sec.movietalk.review.repository;

import com.sec.movietalk.common.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT r FROM Review r
        JOIN com.sec.movietalk.common.domain.movie.MovieCache m
        ON r.movieId = m.movieId
        WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Review> searchByMovieTitleInCache(@Param("keyword") String keyword);

    List<Review> findAllByMovieIdIn(List<Integer> movieIds);

    List<Review> findByUser_UserId(Long userId);
}




