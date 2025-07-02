package com.sec.movietalk.recommendation.dto.tmdb;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TmdbMovieListResponse {
    private int page;
    private List<TmdbMovie> results;
    // 필요 시 total_pages, total_results 추가 가능
}
