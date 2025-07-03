package com.sec.movietalk.popular.repository.projection;

/** 조회 수 집계 결과 */
//MovieViewAggregationRepository 에 정의된 “상세 페이지 조회수” 집계 쿼리 결과
//movieId (영화 ID)
//
//cnt (조회 수)
//에 대응하는 프로젝션
public interface ViewCountProjection {
    int  getMovieId();
    long getCnt();
}