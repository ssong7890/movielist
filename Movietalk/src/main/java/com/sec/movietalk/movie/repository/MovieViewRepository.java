package com.sec.movietalk.movie.repository;

import com.sec.movietalk.common.domain.movie.MovieViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieViewRepository extends JpaRepository<MovieViews, Long> {

    // 특정 영화의 전체 조회수 (총 카운트)
    Long countByMovieId(Integer movieId);

    // 선택: 특정 영화 + 유저 조합 존재 여부 확인
    boolean existsByMovieIdAndUserId(Integer movieId, Long userId);

    // 선택: 특정 영화에 대한 조회 기록 전체 조회
    List<MovieViews> findByMovieId(Integer movieId);
}
