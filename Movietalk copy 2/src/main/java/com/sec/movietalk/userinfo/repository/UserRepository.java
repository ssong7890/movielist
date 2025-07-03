package com.sec.movietalk.userinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sec.movietalk.common.domain.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByEmail(String email);

    Optional<User> findByEmailAndNickname(String email, String nickname);

    User findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.reviewCnt = u.reviewCnt + :inc WHERE u.userId = :id")
    void incrementReviewCount(@Param("id") Long id, @Param("inc") int inc);


    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.commentCnt = u.commentCnt + :inc WHERE u.userId = :id")
    void incrementCommentCount(@Param("id") Long id, @Param("inc") int inc);
}
