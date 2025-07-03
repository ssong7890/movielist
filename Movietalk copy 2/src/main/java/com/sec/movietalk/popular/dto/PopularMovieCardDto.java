package com.sec.movietalk.popular.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 인기 영화 카드 1개를 표현하는 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PopularMovieCardDto {

    /** 1부터 시작하는 순위 */
    private int rank;

    private int movieId;
    private String title;
    private String posterUrl;
    private LocalDate releaseDate;

    /** “조회수”, “리뷰수” 등 카드에 표시할 라벨 */
    private String metricLabel;
    private long   metricValue;

    /** 배우 칩 0 – N개 */
    private List<ActorChip> actorChips;
}
