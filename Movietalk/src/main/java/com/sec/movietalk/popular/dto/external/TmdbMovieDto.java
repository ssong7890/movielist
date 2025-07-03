package com.sec.movietalk.popular.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TmdbMovieDto {

    private int id;
    private String title;

    @JsonProperty("poster_path") //JSON 필드 이름을 지정
    private String posterPath; //JSON 필드 "poster_path" 를 posterPath 자바 필드에 매핑

    private String overview;

    @JsonProperty("release_date")
    private LocalDate releaseDate;
}