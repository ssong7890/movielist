package com.sec.movietalk.common.domain.movie;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "movie_view_daily")



// 이 엔티티의 PK는 MovieViewDailyId 클래스에 정의된 필드들로 이루어짐
// movieId 와 viewDate 두 컬럼을 합쳐서 하나의 기본 키로 사용
@IdClass(MovieViewDailyId.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MovieViewDaily { //일별 영화 조회 수를 저장

    @Id
    @Column(name = "movie_id", nullable = false)
    private Integer movieId;

    @Id
    @Column(name = "view_date", nullable = false)
    private LocalDate viewDate;

    @Column(nullable = false, columnDefinition = "int default 0")

    private Integer cnt;
}
