package com.sec.movietalk.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieDetailDto {

    private String title;

    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    private int runtime;

    private String tagline;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("poster_path")
    private String posterPath;

    private double popularity;

    private double voteAverage;

    private int voteCount;

    private List<Genre> genres;

    @JsonProperty("production_countries")
    private List<ProductionCountry> productionCountries;

    // 내부 클래스: 장르
    @Getter
    @Setter
    public static class Genre {
        private int id;
        private String name;
    }

    // 내부 클래스: 국가
    @Getter
    @Setter
    public static class ProductionCountry {
        @JsonProperty("iso_3166_1")
        private String isoCode;
        private String name;
    }
}
