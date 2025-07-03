package com.sec.movietalk.review.dto;

public record UpdateCommentRequest(
        Long commentId,
        String content
) {}

