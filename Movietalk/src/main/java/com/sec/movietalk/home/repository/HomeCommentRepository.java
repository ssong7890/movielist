package com.sec.movietalk.home.repository;

import com.sec.movietalk.common.domain.comment.Comment;
import com.sec.movietalk.common.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeCommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findTop3ByOrderByLikeCntDesc();

}
