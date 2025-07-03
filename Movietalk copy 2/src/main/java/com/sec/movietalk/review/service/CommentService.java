package com.sec.movietalk.review.service;

import com.sec.movietalk.common.domain.comment.Comment;
import com.sec.movietalk.common.domain.comment.CommentReactions;
import com.sec.movietalk.common.domain.comment.CommentReports;
import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.review.dto.CommentRequest;
import com.sec.movietalk.review.dto.CommentResponse;
import com.sec.movietalk.review.repository.CommentReactionsRepository;
import com.sec.movietalk.review.repository.CommentReportsRepository;
import com.sec.movietalk.review.repository.CommentRepository;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final CommentReactionsRepository reactRepo;
    private final CommentReportsRepository reportRepo;
    private final UserRepository userRepo;

    /** 신고 누적 기준 **/
    private static final int MAX_REPORTS = 5;

    /** 1. 특정 리뷰의 최상위 댓글(숨김되지 않은) + 대댓글 포함 트리 조회 **/
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long reviewId) {
        return commentRepo
                .findByReview_ReviewIdAndParentIsNullOrderByCreatedAt(reviewId).stream()
                // 신고 많은 댓글은 화면에서만 걸러냄
                .filter(c -> c.getReports().size() < MAX_REPORTS)
                .map(this::toDtoWithReplies)
                .collect(Collectors.toList());
    }

    /** 2. 새 댓글 작성 **/
    @Transactional
    public CommentResponse addComment(CommentRequest req, User currentUser) {
        Comment c = Comment.builder()
                .review(Review.builder().reviewId(req.reviewId()).build())
                .parent(null)
                .user(currentUser)
                .content(req.content())
                .build();
        commentRepo.save(c);

        Long userId = currentUser.getId();

        userRepo.incrementCommentCount(userId, 1);

        return toDtoWithReplies(c);
    }

    /** 3. 대댓글 작성 (단, 최상위 댓글에만 허용) **/
    @Transactional
    public CommentResponse reply(CommentRequest req, User currentUser) {
        Comment parent = commentRepo.findById(req.parentId())
                .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));

        // ▶ 대댓글에는 추가 답글 금지
        if (parent.getParent() != null) {
            throw new IllegalArgumentException("대댓글에는 답글을 달 수 없습니다.");
        }

        Comment c = Comment.builder()
                .review(parent.getReview())
                .parent(parent)
                .user(currentUser)
                .content(req.content())
                .build();
        commentRepo.save(c);
        return toDtoWithReplies(c);
    }

    /** 4. 좋아요/싫어요 반응 처리 **/
    @Transactional
    public void react(Long commentId, CommentReactions.ReactionType reactionType, User currentUser) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 기존 반응 제거
        reactRepo.findByComment_CommentIdAndUser_UserId(commentId, currentUser.getUserId())
                .ifPresent(reactRepo::delete);

        // 새 반응 저장
        CommentReactions r = new CommentReactions();
        r.setComment(comment);
        r.setUser(currentUser);
        r.setReaction(reactionType);
        reactRepo.save(r);

        // 카운트 동기화
        int likes = (int) reactRepo.countByComment_CommentIdAndReaction(
                commentId, CommentReactions.ReactionType.like);
        int dislikes = (int) reactRepo.countByComment_CommentIdAndReaction(
                commentId, CommentReactions.ReactionType.dislike);
        comment.setLikeCnt(likes);
        comment.setDislikeCnt(dislikes);
    }

    /** 5. 댓글 신고 처리 (DB 삭제 없이 숨김만) **/
    @Transactional
    public void report(Long commentId, String reason, User currentUser) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 중복 신고 방지
        if (reportRepo.findByComment_CommentIdAndUser_UserId(commentId, currentUser.getUserId()).isPresent()) {
            throw new RuntimeException("이미 신고한 댓글입니다.");
        }

        // 신고 저장
        CommentReports rpt = CommentReports.builder()
                .comment(c)
                .user(currentUser)
                .reason(reason)
                .build();
        reportRepo.save(rpt);

        // ▶ 삭제 로직 제거: DB에는 그대로 두고, getComments() 필터로만 숨김 처리
    }

    /** 6. 댓글 채택 처리 (리뷰 작성자만) **/
    @Transactional
    public void adopt(Long commentId, User currentUser) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!c.getReview().getUser().equals(currentUser)) {
            throw new AccessDeniedException("리뷰 작성자만 채택할 수 있습니다.");
        }
        c.markAsAccepted();
    }

    /** 7. 댓글 수정 (작성자만) **/
    @Transactional
    public CommentResponse updateComment(Long reviewId, Long commentId, String newContent, Long userId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!c.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }
        c.setContent(newContent);
        return toDtoWithReplies(c);
    }

    /** 8. 댓글 삭제 (작성자 또는 리뷰 작성자) **/
    @Transactional
    public void deleteComment(Long reviewId, Long commentId, Long userId) {
        log.info("deleteComment called reviewId={} commentId={} userId={}", reviewId, commentId, userId);

        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 권한 검사 로그
        log.info("commentAuthor={} reviewAuthor={}",
                c.getUser().getUserId(),
                c.getReview().getUser().getUserId());

        // 권한 체크
        if (!c.getUser().getUserId().equals(userId) &&
                !c.getReview().getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        // 작성자 카운트 감소
        userRepo.incrementCommentCount(c.getUser().getUserId(), -1);

        // JPA cascade + DB CASCADE 로 한 번에 삭제
        commentRepo.deleteById(commentId);

        log.info("deleteComment success commentId={}", commentId);
    }


    /** 댓글 → DTO 변환 + 재귀적으로 대댓글 포함(신고 많은 자식 숨김) **/
    private CommentResponse toDtoWithReplies(Comment c) {
        List<CommentResponse> replies = commentRepo
                .findByParent_CommentIdOrderByCreatedAt(c.getCommentId()).stream()
                .filter(child -> child.getReports().size() < MAX_REPORTS)
                .map(this::toDtoWithReplies)
                .collect(Collectors.toList());

        return new CommentResponse(
                c.getCommentId(),
                c.getUser().getUserId(),
                c.getUser().getNickname(),
                c.getContent(),
                c.getCreatedAt(),
                c.getLikeCnt(),
                c.getDislikeCnt(),
                c.getAccepted(),
                replies
        );
    }
}
