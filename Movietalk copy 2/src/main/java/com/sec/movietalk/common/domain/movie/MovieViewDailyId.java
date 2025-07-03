package com.sec.movietalk.common.domain.movie;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * movie_view_daily PK = movieId + viewDate  => 복합 키 클래스
 */
@Getter
@NoArgsConstructor          // JPA 규약
@EqualsAndHashCode          // PK 비교에 필요
public class MovieViewDailyId implements Serializable {

    //이 클래스의 필드(movieId, viewDate) 순서와 이름이 엔티티의 @Id 필드와 정확히 일치해야함
    private Integer movieId;
    private LocalDate viewDate;

    public MovieViewDailyId(Integer movieId, LocalDate viewDate) {
        this.movieId = movieId;
        this.viewDate = viewDate;
    }
}
