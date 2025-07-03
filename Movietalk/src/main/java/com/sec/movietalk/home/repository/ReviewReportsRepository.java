package com.sec.movietalk.home.repository;

import com.sec.movietalk.common.domain.review.ReviewReports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportsRepository extends JpaRepository<ReviewReports, Long> {
}
