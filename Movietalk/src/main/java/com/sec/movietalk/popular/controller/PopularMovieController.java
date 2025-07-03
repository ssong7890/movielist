package com.sec.movietalk.popular.controller;

import com.sec.movietalk.common.util.RankingPeriod;
import com.sec.movietalk.common.util.SortType;
import com.sec.movietalk.popular.dto.PopularMoviePageRequest;
import com.sec.movietalk.popular.dto.PopularMoviePageResponse;
import com.sec.movietalk.popular.service.PopularMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/popular")
@RequiredArgsConstructor
public class PopularMovieController {

    private final PopularMovieService service;

    /** 인기 영화 리스트 JSON */
    @GetMapping
    public ResponseEntity<PopularMoviePageResponse> getPopularMovies(
            @RequestParam(defaultValue = "VIEWS")  SortType sort,
            @RequestParam(defaultValue = "DAILY")  RankingPeriod period,
            @RequestParam(defaultValue = "0")      int page,
            @RequestParam(defaultValue = "20")     int size) {

        PopularMoviePageRequest req = PopularMoviePageRequest.builder()
                .sort(sort)
                .period(period)
                .page(page)
                .size(size)
                .build();

        return ResponseEntity.ok(service.getPopularMovies(req)); //HTTP 200 상태와 함께 JSON 바디를 반환
    }
}
