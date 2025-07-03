package com.sec.movietalk.review.dto;

import com.sec.movietalk.common.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Integer movieId;
    private Long userId;
    private String content;
    private int likeCount;
    private int dislikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 추가
    private String author;
    private String movieTitle;
    private String moviePosterUrl;

    public static ReviewResponse fromEntity(
            Review review,
            String author,
            String movieTitle,
            String moviePosterUrl
    ) {
        return new ReviewResponse(
                review.getId(),
                review.getMovieId(),
                review.getUserId(),
                review.getContent(),
                review.getLikeCount(),
                review.getDislikeCount(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                author,
                movieTitle,
                moviePosterUrl
        );
    }
}









