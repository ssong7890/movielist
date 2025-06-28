package com.sec.movietalk.movie.controller;

import com.sec.movietalk.movie.dto.MovieDetailDto;
import com.sec.movietalk.movie.dto.MovieResponseDto;
import com.sec.movietalk.movie.dto.MovieSearchResultDto;
import com.sec.movietalk.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movies")
    public String showMovieList(Model model) {
        List<MovieResponseDto> movies = movieService.getAllMoviesSortedByReleaseDate();
        model.addAttribute("movies", movies);
        return "movie/list"; // list.html (Thymeleaf 템플릿) 또는 JSON 응답
    }
    @GetMapping("/movies/{id}")
    public String getMovieDetail(@PathVariable Long id, Model model) {

        MovieResponseDto movie = movieService.getMovieById(id);
        MovieDetailDto detail = movieService.getMovieDetailFromTmdb(id);

        model.addAttribute("movie", movie);
        model.addAttribute("detail", detail);   // TMDb 상세 정보

        return "movie/detail"; // detail.html 템플릿 반환
    }

    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam String keyword, Model model) {
        List<MovieSearchResultDto> results = movieService.searchMoviesFromTmdb(keyword);
        model.addAttribute("movies", results);       // 검색 결과 리스트
        model.addAttribute("keyword", keyword);      // 검색 키워드 (뷰에서 재사용할 수 있음)
        return "movie/list";                         // 기존 영화 목록 뷰 재사용
    }


}
