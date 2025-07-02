 package com.sec.movietalk.movie.service;

import com.sec.movietalk.common.domain.movie.MovieViews;
import com.sec.movietalk.movie.repository.MovieViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieViewService {

    private final MovieViewRepository movieViewRepository;

    /**
     * 영화 상세 페이지 진입 시 조회 기록을 저장
     * 로그인하지 않은 사용자도 저장 가능 (userId는 nullable)
     */
    @Transactional
    public void recordView(Integer movieId, Long userId) {
        MovieViews view = MovieViews.builder()
                .movieId(movieId)
//                .userId(userId) // 로그인 안 했을 경우 null
                .build();

        movieViewRepository.save(view); // viewed_at은 @PrePersist로 자동 설정
    }

    /**
     * 누적 조회수 가져오기
     */
    public Long getViewCount(Integer movieId) {
        return movieViewRepository.countByMovieId(movieId);
    }

    /**
     * 유저가 특정 영화에 대해 이미 조회한 적 있는지 확인 (옵션용)
     */
    public boolean hasViewed(Integer movieId, Long userId) {
        return movieViewRepository.existsByMovieIdAndUserId(movieId, userId);
    }
}
