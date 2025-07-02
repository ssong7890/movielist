package com.sec.movietalk.recommendation.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RecommendationRes {
    private List<MovieRecommendation> recommendations;
    private LocalDateTime generated_at;
}