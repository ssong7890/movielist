package com.sec.movietalk.home.service;

import com.sec.movietalk.common.domain.comment.Comment;
import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.common.domain.review.Review;
import com.sec.movietalk.home.dto.HomeCommentDto;
import com.sec.movietalk.home.dto.HomeMovieDto;
import com.sec.movietalk.home.dto.HomeReviewDto;
import com.sec.movietalk.home.repository.HomeCommentRepository;
import com.sec.movietalk.home.repository.HomeMovieRepository;
import com.sec.movietalk.home.repository.HomeReviewRepository;
import com.sec.movietalk.home.repository.MovieCache3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeMovieRepository movieViewsRepo;
    private final MovieCache3Repository movieCacheRepo;
    private final HomeReviewRepository homeViewsRepo;
    private final HomeCommentRepository homeCommentsRepo;

    public List<HomeMovieDto> getTop4MoviesByViews() {

        Pageable pageable = PageRequest.of(0, 4);
        Page<HomeMovieRepository.MovieViewsCountProjection> top4Page = movieViewsRepo.findTop4ByMovieIdCount(pageable);

        List<HomeMovieDto> result = new ArrayList<>();
        for (var proj : top4Page.getContent()) {
            Integer movieId = proj.getMovieId();
            MovieCache movie = movieCacheRepo.findById(movieId).orElse(null);
            if (movie != null) {
                result.add(new HomeMovieDto(
                        movieId,
                        movie.getTitle(),
                        movie.getPosterUrl()
                ));
            }
        }
        return result;
    }

    public List<HomeReviewDto> getTop3Reviews() {

        List<Review> reviews = homeViewsRepo.findTop3ByOrderByLikeCountDesc();

        List<HomeReviewDto> dtos = new ArrayList<>();
        for (Review r : reviews) {

            Integer movieId = r.getMovieId();
            MovieCache movie = movieCacheRepo.findById(movieId).orElse(null);
            String movieTitle = Objects.requireNonNull(movie).getTitle();

            dtos.add(new HomeReviewDto(

                    r.getReviewId(),
                    movieTitle,
                    r.getContent(),
                    r.getLikeCount()

            ));
        }

        return dtos;
    }

    public List<HomeCommentDto> getTop3Comments() {

        List<Comment> comments = homeCommentsRepo.findTop3ByOrderByLikeCntDesc();

        List<HomeCommentDto> dtos = new ArrayList<>();

        for (Comment r : comments) {

            String nickname = r.getUser().getNickname();

            dtos.add(new HomeCommentDto(

                    r.getCommentId(),
                    r.getReview().getReviewId(),
                    nickname,
                    r.getContent(),
                    r.getLikeCnt()

            ));
        }

        return dtos;
    }

}
