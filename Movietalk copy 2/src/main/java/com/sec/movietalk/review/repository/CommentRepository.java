package com.sec.movietalk.review.repository;

import com.sec.movietalk.common.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /** 1) 특정 리뷰의 최상위 댓글(=parent null) 리스트 조회 **/
    List<Comment> findByReview_ReviewIdAndParentIsNullOrderByCreatedAt(Long reviewId);

    /** 2) 특정 댓글의 대댓글 리스트 조회 **/
    List<Comment> findByParent_CommentIdOrderByCreatedAt(Long parentId);

    /** 3) 유저의 모든 댓글 조회 **/
    List<Comment> findByUser_UserId(Long userId);

    /** 4) commentId + reviewId 로 보안 강화하여 단일 댓글 조회 **/
    Optional<Comment> findByCommentIdAndReview_ReviewId(Long commentId, Long reviewId);

    /** 5) 부모 댓글 삭제 시 하위 대댓글 일괄 삭제 **/
    void deleteByParent_CommentId(Long parentId);

    int countByReview_ReviewId(Long reviewId);

    List<Comment> findByReview_ReviewId(Long attr0);

    List<Comment> findByParent_CommentId(Long commentId);
}
