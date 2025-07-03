package com.sec.movietalk.review.repository;

import com.sec.movietalk.common.domain.comment.CommentReports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReportsRepository extends JpaRepository<CommentReports, Long> {

    // 1) 유저와 댓글에 따른 신고 중복 방지
    Optional<CommentReports> findByComment_CommentIdAndUser_UserId(Long commentId, Long userId);

    // 2) 특정 댓글에 대한 누적 신고 수 조회
    long countByComment_CommentId(Long commentId);

    void deleteByComment_CommentId(Long commentId);
}
