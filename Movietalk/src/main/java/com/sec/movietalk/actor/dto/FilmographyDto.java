package com.sec.movietalk.actor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilmographyDto {
    private String title;
    private String character;
    private String releaseDate;
    private String posterPath;
    private String overview;
    private Double voteAverage;
}
