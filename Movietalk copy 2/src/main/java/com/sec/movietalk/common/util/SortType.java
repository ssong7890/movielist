package com.sec.movietalk.common.util;

/**
 * 인기 영화 랭킹 정렬 기준
 * - VIEWS   : 영화 상세 페이지 조회 수
 * - REVIEWS : 해당 영화 리뷰 수
 */
public enum SortType {

    /** 조회 수 기준(내림차순) */
    VIEWS,

    /** 리뷰 수 기준(내림차순) */
    REVIEWS;

    /**
     * 문자열 파라미터 → Enum 매핑
     * 잘못된 값·null 은 기본값 VIEWS 로 대체
     */
    public static SortType of(String value) {
        if (value == null) return VIEWS;
        try {
            return SortType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return VIEWS;
        }
    }
}
