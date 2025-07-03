package com.sec.movietalk.common.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_favorite",
        uniqueConstraints = @UniqueConstraint(name = "uniq_user_movie", columnNames = {"user_id", "movie_id"}),
        indexes = @Index(name = "idx_user_favorite", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userFavoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "movie_id", nullable = false)
    private Integer movieId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }


    public UserFavorite(User user, Integer movieId) {
        this.user = user;
        this.movieId = movieId;
    }

}
