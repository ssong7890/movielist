package com.sec.movietalk.popular.dto;

import lombok.*;

/**
 * 페이징 정보 메타
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageMeta {

    private int  page;          // 0-based
    private int  size;          // 페이지 크기
    private long totalElements; // 전체 레코드 수
    private int  totalPages;    // 전체 페이지 수
}
