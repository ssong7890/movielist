package com.sec.movietalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchResultDto {

    private Integer id;              // ✅ TMDB 영화 ID (상세 페이지 이동용)
    private String title;         // 영화 제목
    private String overview;      // 줄거리 요약
    private String posterPath;    // 포스터 이미지 경로
    private String releaseDate;   // 개봉일
    private String posterUrl;     // 전체 포스터 이미지 URL
    private boolean adult;        // ✅ 성인 콘텐츠 여부 (true = 제한 필요)

    private Double popularity;    // ✅ 인기 점수 (인기순 정렬용 추가)
}
