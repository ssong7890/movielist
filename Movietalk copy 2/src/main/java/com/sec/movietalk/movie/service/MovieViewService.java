package com.sec.movietalk.movie.service;

import com.sec.movietalk.common.domain.movie.MovieViews;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.movie.repository.MovieViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieViewService {

    private final MovieViewRepository movieViewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 영화 상세 페이지 진입 시 조회 기록을 저장
     * 로그인하지 않은 사용자도 저장 가능 (userId는 nullable)
     * 존재하지 않는 userId일 경우 user_id는 null로 기록
     */
    @Transactional
    public void recordView(Integer movieId, Long userId) {
        User user = null;

        if (userId != null) {
            try {
                user = entityManager.getReference(User.class, userId);
            } catch (EntityNotFoundException e) {
                log.warn("❗ 존재하지 않는 userId: {}", userId);
                user = null;
            }
        }

        MovieViews view = MovieViews.builder()
                .movieId(movieId)
                .user(user) // user가 null이어도 OK
                .build();

        movieViewRepository.save(view); // viewedAt은 @PrePersist로 자동 설정
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
