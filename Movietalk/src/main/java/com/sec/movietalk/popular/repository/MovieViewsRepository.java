// com/sec/movietalk/popular/repository/MovieViewsRepository.java
package com.sec.movietalk.popular.repository;

import com.sec.movietalk.common.domain.movie.MovieViews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieViewsRepository extends JpaRepository<MovieViews, Long> {
}
