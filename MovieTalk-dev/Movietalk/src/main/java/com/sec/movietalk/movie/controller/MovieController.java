package com.sec.movietalk.movie.controller;

import com.sec.movietalk.movie.dto.MovieResponseDto;
import com.sec.movietalk.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}

//    @GetMapping("/movies")
//    public String showMovieList(Model model) {
//        List<MovieResponseDto> movies = new ArrayList<>();
//        movies.add(MovieResponseDto.builder()
//                .title("인셉션")
//                .posterUrl("https://example.com/poster.jpg")
//                .releaseDate(LocalDate.of(2025, 7, 1))
//                .build());
//        model.addAttribute("movies", movies);
//        return "movie/list";
//    }
//}