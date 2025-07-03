package com.sec.movietalk.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieRecommendation {
    private Long movie_id;
    private String title;
    @JsonProperty("poster_path")
    private String poster_path;

    @JsonProperty("vote_average")
    private Double vote_average;
    private Double popularity;
}