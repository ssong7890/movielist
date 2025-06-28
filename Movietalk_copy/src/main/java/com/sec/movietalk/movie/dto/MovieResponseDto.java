package com.sec.movietalk.movie.dto;

import com.sec.movietalk.common.domain.movie.MovieCache;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MovieResponseDto {
    private Integer movieId;
    private String title;
    private String posterUrl;
    private LocalDate releaseDate;

    public static MovieResponseDto fromEntity(MovieCache movie) {
        return MovieResponseDto.builder()
                .movieId(movie.getTmdbId())
                .title(movie.getTitle())
                .posterUrl(movie.getPosterUrl())
                .releaseDate(movie.getReleaseDate())
                .build();
    }
}
