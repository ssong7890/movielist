// com/sec/movietalk/review/repository/ReviewReportsRepository.java
package com.sec.movietalk.review.repository;

import com.sec.movietalk.common.domain.review.ReviewReports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewReportsRepository2 extends JpaRepository<ReviewReports, Long> {
    /** 유저·리뷰 중복 신고 방지 **/
    Optional<ReviewReports> findByReview_ReviewIdAndUser_UserId(Long reviewId, Long userId);

    /** 특정 리뷰에 대한 누적 신고 수 조회 **/
    long countByReview_ReviewId(Long reviewId);
}
