package com.sec.movietalk.common.domain.movie;

import com.sec.movietalk.common.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie_views",
        indexes = @Index(name = "idx_movie_viewed_at", columnList = "movie_id, viewed_at"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieViewsId;

    @Column(name = "movie_id")
    private Integer movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    @PrePersist
    public void prePersist() {
        if (viewedAt == null) viewedAt = LocalDateTime.now();
    }
}
