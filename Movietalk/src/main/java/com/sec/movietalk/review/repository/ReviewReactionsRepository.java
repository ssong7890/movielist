// com/sec/movietalk/review/repository/ReviewReactionsRepository.java
package com.sec.movietalk.review.repository;

import com.sec.movietalk.common.domain.review.ReviewReactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewReactionsRepository extends JpaRepository<ReviewReactions, Long> {
    /** 해당 리뷰·유저의 기존 리액션 조회 **/
    Optional<ReviewReactions> findByReview_ReviewIdAndUser_UserId(Long reviewId, Long userId);

    /** 리뷰별 좋아요/싫어요 개수 집계 **/
    long countByReview_ReviewIdAndReaction(Long reviewId, ReviewReactions.ReactionType reaction);
}
