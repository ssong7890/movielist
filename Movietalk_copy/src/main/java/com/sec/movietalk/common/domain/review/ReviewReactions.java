package com.sec.movietalk.common.domain.review;

import com.sec.movietalk.common.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_reactions",
        uniqueConstraints = @UniqueConstraint(name = "uniq_review_user", columnNames = {"review_id", "user_id"}),
        indexes = @Index(name = "idx_review_reaction", columnList = "review_id, reaction"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewReactionsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 7)
    private ReactionType reaction;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public enum ReactionType {
        like, dislike
    }
}
