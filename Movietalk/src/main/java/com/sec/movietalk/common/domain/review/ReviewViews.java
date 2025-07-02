package com.sec.movietalk.common.domain.review;

import com.sec.movietalk.common.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_views",
        uniqueConstraints = @UniqueConstraint(name = "uniq_review_user_date", columnNames = {"review_id", "user_id", "viewedDate"}),
        indexes = @Index(name = "idx_review_viewed_at", columnList = "review_id, viewedAt"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewsId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    @Transient
    private java.time.LocalDate viewedDate; // 날짜만 비교용 (Unique 제약용)

    @PrePersist
    public void prePersist() {
        if (viewedAt == null) {
            viewedAt = LocalDateTime.now();
        }
        viewedDate = viewedAt.toLocalDate();
    }
}
