package com.sec.movietalk.common.domain.comment;

import com.sec.movietalk.common.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_reports",
        uniqueConstraints = @UniqueConstraint(name = "uniq_comment_report", columnNames = {"comment_id", "user_id"}),
        indexes = @Index(name = "idx_comment_report", columnList = "comment_id, created_at"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentReportsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

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
