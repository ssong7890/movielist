package com.sec.movietalk.review.controller;

import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.common.util.UserUtil;
import com.sec.movietalk.review.dto.ReportRequest;
import com.sec.movietalk.review.service.CommentService;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/{commentId}/reports")
@RequiredArgsConstructor
public class CommentReportController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    /** 5) 댓글 신고 **/
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)   // 성공 시 204 반환
    public void report(
            @PathVariable Long commentId,
            @RequestBody ReportRequest req,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = UserUtil.extractUserId(principal);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        commentService.report(commentId, req.reason(), currentUser);
    }

    /**
     * IllegalArgumentException → 400 Bad Request 처리
     * (예: 이미 신고했거나, 대댓글 답글 금지 예외)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
