// com/sec/movietalk/popular/repository/MovieViewDailyRepository.java
package com.sec.movietalk.popular.repository;

import com.sec.movietalk.common.domain.movie.MovieViewDaily;
import com.sec.movietalk.common.domain.movie.MovieViewDailyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieViewDailyRepository
        extends JpaRepository<MovieViewDaily, MovieViewDailyId> {
}
