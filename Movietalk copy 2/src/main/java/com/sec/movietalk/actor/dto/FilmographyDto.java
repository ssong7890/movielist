package com.sec.movietalk.actor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FilmographyDto {

    private Long id;
    private String title;
    private String character;
    private String releaseDate;
    private String posterPath;
    private String overview;
    private Double voteAverage;

    // 생성자
    public FilmographyDto(Long id, String title, String character,
                          String releaseDate, String posterPath,
                          String overview, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.character = character;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getTitle() { return title; }
    public String getCharacter() { return character; }
    public String getReleaseDate() { return releaseDate; }
    public String getPosterPath() { return posterPath; }
    public String getOverview() { return overview; }
    public Double getVoteAverage() { return voteAverage; }
}

