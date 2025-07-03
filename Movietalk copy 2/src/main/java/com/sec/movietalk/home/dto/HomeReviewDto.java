package com.sec.movietalk.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeReviewDto {

    private Long reviewId;
    private String movieTitle;
    private String content;
    private Integer likeCount;

}
