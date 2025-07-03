package com.sec.movietalk.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * 온보딩 영화 카드 DTO
 */
@Getter
@Builder
public class OnboardingMovie {

    /** MovieCache.movieId */
    private Integer movieId;

    private String  title;
    @JsonProperty("posterUrl")   // JSON 필드명 고정
    private String  posterUrl;
}
