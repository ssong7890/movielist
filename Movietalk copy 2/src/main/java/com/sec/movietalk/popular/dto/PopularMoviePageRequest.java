package com.sec.movietalk.popular.dto;

import com.sec.movietalk.common.util.RankingPeriod;
import com.sec.movietalk.common.util.SortType;
import lombok.*;

/**
 * 인기 영화 리스트 조회 시 컨트롤러가 받는 파라미터 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PopularMoviePageRequest {

    /** 정렬 기준(VIEWS / REVIEWS) */
    private SortType sort;

    /** 집계 기간(DAILY / WEEKLY / MONTHLY / ALLTIME) */
    private RankingPeriod period;

    /** 0-based page index */
    private int page;

    /** page size (default 20 정도) */
    private int size;
}
