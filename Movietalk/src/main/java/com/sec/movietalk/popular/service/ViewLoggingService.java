package com.sec.movietalk.popular.service;

import com.sec.movietalk.common.domain.movie.MovieViews;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.popular.repository.MovieViewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 영화 상세 페이지 조회 기록 INSERT
 */
@Service
@RequiredArgsConstructor
//사용자가 영화 상세 페이지에 들어갈 때마다 그 조회 이벤트를 movie_views 테이블에 기록(insert)
public class ViewLoggingService {

    private final MovieViewsRepository repo;

    @Transactional
    public void logView(int movieId, Long userId) {

        // (User 조회 로직이 있다면 교체) -----------
        User user = null;
        if (userId != null) {
            user = new User();         // stub
            user.setUserId(userId);
        } //User 엔티티 연결
        // ---------------------------------------

        MovieViews entity = MovieViews.builder()
                .movieId(movieId)
                .user(user)
                .build();

        repo.save(entity);
    }
}
