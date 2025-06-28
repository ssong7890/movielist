package com.sec.movietalk.common.domain.movie;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_cache",
        indexes = @Index(name = "idx_movie_release", columnList = "release_date"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCache {

    @Id
    @Column(name = "movie_id")
    private Integer movieId;

//    @Id
//    @Column(name = "movie_id")
//    private Integer tmdbId;

    @Column(name = "tmdb_id")
    private Integer tmdbId;

    @Column(length = 255)
    private String title;

    @Lob
    private String posterUrl;

    @Lob
    private String overview;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
