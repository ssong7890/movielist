package com.sec.movietalk.popular.repository.projection;

/** 리뷰 수 집계 결과 */
//ReviewAggregationRepository 에 정의된 JPQL/네이티브 쿼리 결과 중
//movieId (영화 ID), cnt (해당 영화의 리뷰 수)
//두 컬럼만 꺼내서 이 인터페이스 구현체로 매핑
public interface ReviewCountProjection {
    int  getMovieId();
    long getCnt();
}


