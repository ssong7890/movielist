package com.sec.movietalk.review.controller;

import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.common.util.UserUtil;
import com.sec.movietalk.review.dto.CommentRequest;
import com.sec.movietalk.review.dto.UpdateCommentRequest;
import com.sec.movietalk.review.dto.CommentResponse;
import com.sec.movietalk.review.service.CommentService;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/{reviewId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;  // User 엔티티 로딩용

    /** 1) 해당 리뷰의 댓글 리스트 조회 **/
    @GetMapping
    public List<CommentResponse> list(@PathVariable Long reviewId) {
        return commentService.getComments(reviewId);
    }

    /** 2) 새 댓글 작성 **/
    @PostMapping
    public CommentResponse add(
            @PathVariable Long reviewId,
            @RequestBody CommentRequest req,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = UserUtil.extractUserId(principal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        CommentRequest payload = new CommentRequest(
                reviewId,
                null,
                req.content()
        );
        return commentService.addComment(payload, user);
    }

    /** 3) 대댓글 작성 **/
    @PostMapping("/{parentId}/reply")
    public CommentResponse reply(
            @PathVariable Long reviewId,
            @PathVariable Long parentId,
            @RequestBody CommentRequest req,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = UserUtil.extractUserId(principal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        CommentRequest payload = new CommentRequest(
                reviewId,
                parentId,
                req.content()
        );
        return commentService.reply(payload, user);
    }

    /** 4) 댓글 수정 **/
    @PatchMapping("/{commentId}")
    public CommentResponse update(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest req,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = UserUtil.extractUserId(principal);
        return commentService.updateComment(reviewId, commentId, req.content(), userId);
    }

    /** 5) 댓글 삭제 **/
    @DeleteMapping("/{commentId}")
    public void delete(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = UserUtil.extractUserId(principal);
        commentService.deleteComment(reviewId, commentId, userId);
    }
}
