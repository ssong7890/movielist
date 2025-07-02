package com.sec.movietalk.review.service;

import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.recommendation.repository.MovieCacheRepository;
import com.sec.movietalk.review.dto.ReviewCreateRequest;
import com.sec.movietalk.review.dto.ReviewListResponse;
import com.sec.movietalk.review.dto.ReviewResponse;
import com.sec.movietalk.review.dto.ReviewUpdateRequest;
import com.sec.movietalk.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieCacheRepository movieCacheRepository;

    @Transactional(readOnly = true)
    public List<ReviewListResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        // movieId → 영화 제목 맵 생성
        Map<Integer, String> titleMap = movieCacheRepository
                .findAllById(reviews.stream()
                        .map(Review::getMovieId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(MovieCache::getMovieId, MovieCache::getTitle));

        // DTO 변환
        return reviews.stream()
                .map(r -> {
                    String title  = titleMap.getOrDefault(r.getMovieId(), "제목 없음");
                    String author = r.getUser().getNickname();
                    return ReviewListResponse.fromEntity(r, title, author);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewListResponse> searchByMovieTitle(String keyword) {
        List<Review> reviews = reviewRepository.searchByMovieTitleInCache(keyword);

        Map<Integer, String> titleMap = movieCacheRepository
                .findAllByTitleContainingIgnoreCase(keyword)
                .stream()
                .collect(Collectors.toMap(MovieCache::getMovieId, MovieCache::getTitle));

        return reviews.stream()
                .map(r -> {
                    String title  = titleMap.getOrDefault(r.getMovieId(), "제목 없음");
                    String author = r.getUser().getNickname();
                    return ReviewListResponse.fromEntity(r, title, author);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다. id=" + reviewId));

        // 영화 제목·포스터 URL 조회
        MovieCache cache = movieCacheRepository.findById(review.getMovieId())
                .orElseThrow(() -> new RuntimeException("영화 정보를 찾을 수 없습니다. id=" + review.getMovieId()));

        String title       = cache.getTitle();
        String posterUrl   = cache.getPosterUrl();   // MovieCache 엔티티에 정의된 필드명 사용
        String author      = review.getUser().getNickname();

        return ReviewResponse.fromEntity(review, author, title, posterUrl);
    }

    @Transactional
    public void createReview(ReviewCreateRequest req) {
        // 제목으로 영화 캐시 조회 (부분 일치 → 정확 일치 우선)
        List<MovieCache> movies = movieCacheRepository
                .findAllByTitleContainingIgnoreCase(req.getMovieTitle());

        if (movies.isEmpty()) {
            throw new IllegalArgumentException("등록된 영화가 아닙니다: " + req.getMovieTitle());
        }

        MovieCache movie = movies.stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(req.getMovieTitle()))
                .findFirst()
                .orElse(movies.get(0));

        Review review = Review.builder()
                .movieId(movie.getMovieId())
                .user(new User(req.getUserId()))
                .content(req.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다. id=" + request.getReviewId()));

        review.setContent(request.getContent());
        review.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}