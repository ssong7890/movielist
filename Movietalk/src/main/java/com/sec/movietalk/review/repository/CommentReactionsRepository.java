package com.sec.movietalk.review.repository;

import com.sec.movietalk.common.domain.comment.CommentReactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionsRepository extends JpaRepository<CommentReactions, Long> {
    /** 해당 댓글·유저의 기존 리액션 조회 **/
    Optional<CommentReactions> findByComment_CommentIdAndUser_UserId(Long commentId, Long userId);

    /** 댓글별 좋아요/싫어요 개수 집계 **/
    long countByComment_CommentIdAndReaction(Long commentId, CommentReactions.ReactionType reaction);
}
