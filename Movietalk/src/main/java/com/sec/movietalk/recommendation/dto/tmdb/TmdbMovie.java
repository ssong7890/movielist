package com.sec.movietalk.recommendation.dto.tmdb;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TmdbMovie {
    private int id;
    private String title;

    // TMDB API 필드명 그대로
    private String poster_path;
    private double vote_average;
    private double popularity;
}
