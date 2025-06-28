package com.sec.movietalk.common.domain.comment;

import com.sec.movietalk.common.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_reactions",
        uniqueConstraints = @UniqueConstraint(name = "uniq_comment_user", columnNames = {"comment_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactions{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentReactionsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 7)
    private ReactionType reaction;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public enum ReactionType {
        like, dislike
    }
}
