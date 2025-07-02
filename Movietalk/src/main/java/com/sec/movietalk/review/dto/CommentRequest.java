package com.sec.movietalk.review.dto;

// 댓글 등록·답글 요청

public record CommentRequest(
        Long reviewId,
        Long parentId,
        String content
) {
    public CommentRequest withReviewId(Long reviewId) {
        return new CommentRequest(reviewId, this.parentId, this.content);
    }
    public CommentRequest withParentId(Long parentId) {
        return new CommentRequest(this.reviewId, parentId, this.content);
    }
}
