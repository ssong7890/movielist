package com.sec.movietalk.recommendation.repository;

import com.sec.movietalk.common.domain.movie.MovieCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCacheRepository extends JpaRepository<MovieCache, Integer> {

    boolean existsByMovieId(Integer movieId);


    List<MovieCache> findAllByTitleContainingIgnoreCase(String keyword);

}

