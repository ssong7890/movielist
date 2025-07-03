package com.sec.movietalk.home.service;

import com.sec.movietalk.common.domain.comment.Comment;
import com.sec.movietalk.common.domain.comment.CommentReports;
import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.common.domain.review.ReviewReports;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.home.repository.ReviewReportsRepository;
import com.sec.movietalk.review.repository.CommentReactionsRepository;
import com.sec.movietalk.review.repository.CommentReportsRepository;
import com.sec.movietalk.review.repository.CommentRepository;
import com.sec.movietalk.review.repository.ReviewRepository;
import com.sec.movietalk.userinfo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final CommentReportsRepository commentReportsRepository;
    private final CommentReactionsRepository commentReactionsRepository;
    private final ReviewReportsRepository reviewReportsRepository;

    public Page<User> getUserPage(int page, int size) {

        return userRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Review> getReviewPage(int page, int size) {

        return reviewRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Comment> getCommentPage(int page, int size) {

        return commentRepository.findAll(PageRequest.of(page, size));
    }

    public Page<ReviewReports> getReviewReportsPage(int page, int size) {

        return reviewReportsRepository.findAll(PageRequest.of(page, size));
    }

    public Page<CommentReports> getCommentReportsPage(int page, int size) {

        return commentReportsRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public void deleteReview(Long ReviewId) {

        Review review = reviewRepository.findById(ReviewId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 리뷰가 없습니다: " + ReviewId));
        Long authorId = review.getUser().getUserId();

        List<Comment> comments = commentRepository.findByReview_ReviewId(ReviewId);

        Map<Long, Long> commentCountMap = comments.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getUser().getUserId(),
                        Collectors.counting()
                ));

        commentCountMap.forEach((userId, count) -> {
            userRepository.incrementCommentCount(userId, -count.intValue());
        });

        userRepository.incrementReviewCount(authorId, -1);


        reviewRepository.deleteById(ReviewId);
    }

    public void deleteUser(Long UserId) {

        userRepository.deleteById(UserId);
    }

    @Transactional
    public void deleteComment(Long CommentId) {
        Comment comment = commentRepository.findById(CommentId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 댓글이 없습니다: " + CommentId));

        // 대댓글 작성자들의 댓글 카운트 감소
        comment.getReplies().forEach(reply -> {
            userRepository.incrementCommentCount(reply.getUser().getUserId(), -1);
        });

        // 메인 댓글 작성자의 댓글 카운트 감소
        userRepository.incrementCommentCount(comment.getUser().getUserId(), -1);

        // JPA cascade가 자동으로 대댓글, 반응, 신고 삭제
        commentRepository.deleteById(CommentId);
    }

    public void deleteCommentReport(Long commentReportId) {

        commentReportsRepository.deleteById(commentReportId);
    }

    public void deleteReviewReport(Long reviewReportId) {

        reviewReportsRepository.deleteById(reviewReportId);
    }
}

