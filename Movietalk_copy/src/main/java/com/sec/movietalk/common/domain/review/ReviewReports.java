package com.sec.movietalk.common.domain.review;

import com.sec.movietalk.common.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_reports",
        uniqueConstraints = @UniqueConstraint(name = "uniq_review_report", columnNames = {"review_id", "user_id"}),
        indexes = @Index(name = "idx_review_report", columnList = "review_id, created_at"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewReportsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
