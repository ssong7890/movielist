package com.sec.movietalk.home.repository;

import com.sec.movietalk.common.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findTop3ByOrderByLikeCountDesc();

}
