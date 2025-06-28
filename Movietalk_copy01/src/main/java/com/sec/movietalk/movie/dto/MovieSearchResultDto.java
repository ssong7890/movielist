package com.sec.movietalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchResultDto {

    private String title;         // 영화 제목
    private String overview;      // 줄거리 요약
    private String posterPath;    // 포스터 이미지 경로
    private String releaseDate;   // 개봉일
    private String posterUrl;    // 전체 포스터 이미지 URL

}
