package com.sec.movietalk.common.domain.comment;

import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.common.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment",
        indexes = @Index(name = "idx_comment_review_parent_date", columnList = "review_id, parent_id, created_at"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parent;

    // 자식 댓글(대댓글)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String content;

    @Column(name = "like_cnt", nullable = false, columnDefinition = "int default 0")
    private Integer likeCnt;

    @Column(name = "dislike_cnt", nullable = false, columnDefinition = "int default 0")
    private Integer dislikeCnt;

    @Column(name = "accepted", nullable = false, columnDefinition = "boolean default false")
    private Boolean accepted;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentReactions> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentReports> reports = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        if (this.accepted == null) this.accepted = false;
        if (this.likeCnt == null) this.likeCnt = 0;
        if (this.dislikeCnt == null) this.dislikeCnt = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsAccepted() {
        this.accepted = true;
        this.acceptedAt = LocalDateTime.now();
    }
}
