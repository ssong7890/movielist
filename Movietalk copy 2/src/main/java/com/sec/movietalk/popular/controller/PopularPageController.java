package com.sec.movietalk.popular.controller;

import com.sec.movietalk.common.util.RankingPeriod;
import com.sec.movietalk.common.util.SortType;
import com.sec.movietalk.popular.dto.PopularMoviePageRequest;
import com.sec.movietalk.popular.dto.PopularMoviePageResponse;
import com.sec.movietalk.popular.service.PopularMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PopularPageController {

    private final PopularMovieService service;

    @GetMapping("/popular")
    public String popularPage(
            @RequestParam(defaultValue = "VIEWS")  SortType sort,
            @RequestParam(defaultValue = "DAILY")  RankingPeriod period,
            @RequestParam(defaultValue = "0")      int page,
            @RequestParam(defaultValue = "20")     int size,
            Model model
    ) {
        // 서비스 호출
        PopularMoviePageResponse resp = service.getPopularMovies(
                PopularMoviePageRequest.builder()
                        .sort(sort)
                        .period(period)
                        .page(page)
                        .size(size)
                        .build()
        );
        // 모델에 담기
        model.addAttribute("resp", resp);
        // 뷰: src/main/resources/templates/popular/list.html
        return "popular/list";
    }
}
