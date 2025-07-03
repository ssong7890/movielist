package com.sec.movietalk.review.dto;

import java.time.LocalDateTime;
import java.util.List;

// 댓글 반환용
public record CommentResponse(
        Long id,                 // 댓글 ID
        Long authorId,           // ← 추가: 댓글 작성자 ID
        String authorName,       // 작성자 이름
        String content,
        LocalDateTime createdAt,
        Integer likeCnt,
        Integer dislikeCnt,
        Boolean accepted,
        List<CommentResponse> replies
) {}
