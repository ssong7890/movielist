package com.sec.movietalk.common.domain.user;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(columnDefinition = "int default 0")
    private Integer commentCnt;

    @Column(columnDefinition = "int default 0")
    private Integer reviewCnt;

    // ✅ Review.java에서 사용되는 간단 생성자
    public User(Long userId) {
        this.userId = userId;
    }

    // ✅ getId() 호출용 메서드
    public Long getId() {
        return userId;
    }


    public void setUserId(Long userId) { 
      this.userId = userId; 
    }

}
