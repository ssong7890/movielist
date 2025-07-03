// com/sec/movietalk/review/controller/ReviewReportController.java
package com.sec.movietalk.review.controller;

import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.common.util.UserUtil;
import com.sec.movietalk.review.dto.ReportRequest;
import com.sec.movietalk.review.service.ReviewService;
import com.sec.movietalk.userinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews/{reviewId}/reports")
@RequiredArgsConstructor
public class ReviewReportController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    /** 리뷰 신고 **/
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void report(
            @PathVariable Long reviewId,
            @RequestBody ReportRequest req,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = UserUtil.extractUserId(principal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        reviewService.report(reviewId, req.reason(), user);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onBadRequest(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
