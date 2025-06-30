package com.sec.movietalk.movie.repository;

import com.sec.movietalk.common.domain.movie.MovieCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieCache, Long> {

    // ✅ 페이지네이션 적용된 정렬 조회
    Page<MovieCache> findAllByOrderByReleaseDateDesc(Pageable pageable);
}
