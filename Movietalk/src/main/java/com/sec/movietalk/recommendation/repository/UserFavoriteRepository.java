package com.sec.movietalk.recommendation.repository;
import com.sec.movietalk.common.domain.user.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    // user_id로 선호 영화 개수 확인
    long countByUser_UserId(long userId);

    // user_id로 선호 영화 목록 조회
    List<UserFavorite> findByUser_UserId(long userId);

    // 이미 저장된 영화인지 중복 체크
    boolean existsByUser_UserIdAndMovieId(long userId, Integer movieId);

    // 즐겨찾기 목록 조회
    List<UserFavorite> findAllByUser_UserId(Long user_id);
}
