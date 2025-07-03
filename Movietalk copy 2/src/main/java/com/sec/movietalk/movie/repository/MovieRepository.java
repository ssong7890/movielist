package com.sec.movietalk.movie.repository;

import com.sec.movietalk.common.domain.movie.MovieCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieCache, Integer> {

    // ✅ 정렬된 페이징 조회
    Page<MovieCache> findAllByOrderByReleaseDateDesc(Pageable pageable);

    // 💡 필요 시 커스텀 메서드 추가
    // Optional<MovieEntityCache> findByTitle(String title);
}
