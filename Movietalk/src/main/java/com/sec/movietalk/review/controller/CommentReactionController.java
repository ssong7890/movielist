package com.sec.movietalk.review.controller;

import com.sec.movietalk.common.domain.comment.CommentReactions.ReactionType;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.common.util.UserUtil;
import com.sec.movietalk.review.dto.ReactionRequest;
import com.sec.movietalk.review.service.CommentService;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/{commentId}/reactions")
@RequiredArgsConstructor
public class CommentReactionController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    /** 4) 댓글 좋아요/싫어요 **/
    @PostMapping
    public void react(
            @PathVariable Long commentId,
            @RequestBody ReactionRequest req,
            @AuthenticationPrincipal Object principal
    ) {
        // 1) 토큰에서 사용자 ID 추출
        Long userId = UserUtil.extractUserId(principal);

        // 2) User 엔티티 로딩
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 3) Request의 문자열을 Enum으로 변환
        ReactionType type = ReactionType.valueOf(req.reactionType());

        // 4) 서비스 호출
        commentService.react(commentId, type, currentUser);
    }
}
