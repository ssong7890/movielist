package com.sec.movietalk.movie.repository;

import com.sec.movietalk.common.domain.movie.MovieCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieCache, Long> {
    List<MovieCache> findAllByOrderByReleaseDateDesc();
}
