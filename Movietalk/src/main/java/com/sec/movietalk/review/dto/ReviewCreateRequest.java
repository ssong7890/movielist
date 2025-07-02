package com.sec.movietalk.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    private Long userId;
    private String movieTitle;   // Integer movieId → String movieTitle 로 변경
    private String content;
}
