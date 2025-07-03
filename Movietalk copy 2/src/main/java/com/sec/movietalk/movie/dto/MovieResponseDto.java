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
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .posterUrl(movie.getPosterUrl())
                .releaseDate(movie.getReleaseDate())
                .build();

    }

    // ✅ 추가된 부분: 공통 id 접근자
    public Long getId() {
        return this.movieId != null ? this.movieId.longValue() : null;
    }

}
