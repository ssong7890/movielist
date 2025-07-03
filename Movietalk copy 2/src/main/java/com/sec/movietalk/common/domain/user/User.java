package com.sec.movietalk.common.domain.user;

import com.sec.movietalk.common.domain.comment.ActorComment;
import com.sec.movietalk.common.domain.comment.Comment;
import com.sec.movietalk.common.domain.comment.CommentReports;
import com.sec.movietalk.common.domain.movie.MovieViews;
import com.sec.movietalk.common.domain.movie.MovieViewDaily;

import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.common.domain.review.ReviewReports;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  // user_id 컬럼명과 매핑

    @Column(length = 120, unique = true, nullable = false)
    private String email;

    @Column(length = 60)
    private String nickname;

    @Column(length = 60)
    private String password;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer commentCnt = 0;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer reviewCnt  = 0;

    @Column(columnDefinition = "varchar(20) default 'USER'")
    private String role = "USER";

    // ✅ Review.java에서 사용되는 간단 생성자
    public User(Long userId) {
        this.userId = userId;
    }

    // ✅ getId() 호출용 메서드
    public Long getId() {
        return userId;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserFavorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MovieViews> movieViews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ActorComment> actorComments = new ArrayList<>();





    public void setUserId(Long userId) { 
      this.userId = userId; 
    }

}
