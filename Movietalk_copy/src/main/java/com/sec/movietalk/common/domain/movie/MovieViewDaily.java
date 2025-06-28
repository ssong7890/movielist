package com.sec.movietalk.common.domain.movie;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "movie_view_daily")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieViewDaily {

    @Id
    @Column(name = "movie_id")
    private Integer movieId;

    @Id
    @Column(name = "view_date")
    private LocalDate viewDate;

    private Integer cnt;
}
