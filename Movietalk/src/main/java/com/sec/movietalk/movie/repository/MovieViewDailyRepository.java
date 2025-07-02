package com.sec.movietalk.movie.repository;

import com.sec.movietalk.common.domain.movie.MovieViewDaily;
import com.sec.movietalk.common.domain.movie.MovieViewDailyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieViewDailyRepository extends JpaRepository<MovieViewDaily, MovieViewDailyId> {

    /**
     * 특정 영화의 특정 날짜에 대한 조회수 기록 조회
     */
    Optional<MovieViewDaily> findByMovieIdAndViewDate(Integer movieId, LocalDate viewDate);

    /**
     * 오늘의 인기 영화 (상위 N개)
     */
    List<MovieViewDaily> findTop10ByViewDateOrderByCntDesc(LocalDate viewDate);

    /**
     * 특정 날짜 범위 동안 해당 영화의 총 조회수
     */
    List<MovieViewDaily> findByMovieIdAndViewDateBetween(Integer movieId, LocalDate start, LocalDate end);

    /**
     * 특정 날짜 기준 모든 영화의 일별 조회수 데이터
     */
    List<MovieViewDaily> findByViewDate(LocalDate viewDate);
}
