package com.sec.movietalk.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeMovieDto {
    private Integer movieId;
    private String title;
    private String posterUrl;
}

