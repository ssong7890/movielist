// com/sec/movietalk/review/controller/ReviewReactionController.java
package com.sec.movietalk.review.controller;

import com.sec.movietalk.common.domain.review.ReviewReactions.ReactionType;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.common.util.UserUtil;
import com.sec.movietalk.review.dto.ReactionRequest;
import com.sec.movietalk.review.service.ReviewService;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews/{reviewId}/reactions")
@RequiredArgsConstructor
public class ReviewReactionController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    /** 리뷰 좋아요/싫어요 **/
    @PostMapping
    public void react(
            @PathVariable Long reviewId,
            @RequestBody ReactionRequest req,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = UserUtil.extractUserId(principal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        ReactionType type = ReactionType.valueOf(req.reactionType());
        reviewService.react(reviewId, type, user);
    }
}
