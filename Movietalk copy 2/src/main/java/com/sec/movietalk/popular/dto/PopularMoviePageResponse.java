package com.sec.movietalk.popular.dto;

import lombok.*;

import java.util.List;

/**
 * 인기 영화 페이징 응답 DTO (JSON 반환용)
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PopularMoviePageResponse {

    private List<PopularMovieCardDto> content;
    private PageMeta meta;
}
