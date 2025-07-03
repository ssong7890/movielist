package com.sec.movietalk.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    private Long userId;
    private Integer movieId;  // TMDB에서 선택된 영화 ID
    private String content;
}
