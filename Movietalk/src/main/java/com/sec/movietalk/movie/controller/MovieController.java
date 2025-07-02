package com.sec.movietalk.movie.controller;

import com.sec.movietalk.client.TmdbClient;
import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.movie.dto.MovieDetailDto;
import com.sec.movietalk.movie.dto.MovieResponseDto;
import com.sec.movietalk.movie.dto.MovieSearchResultDto;
import com.sec.movietalk.movie.service.MovieCacheService;
import com.sec.movietalk.movie.service.MovieService;
import com.sec.movietalk.movie.service.MovieViewService;
import com.sec.movietalk.movie.service.MovieViewDailyService; // ✅ 추가된 import
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final TmdbClient tmdbClient;
    private final MovieCacheService movieCacheService;
    private final MovieViewService movieViewService;          // ✅ 누적 조회수 저장
    private final MovieViewDailyService movieViewDailyService; // ✅ 일일 조회수 저장

    @GetMapping("/movies")
    public String showMovieList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<MovieResponseDto> movies = movieService.getPagedMovies(page);
        model.addAttribute("movies", movies.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movies.getTotalPages());
        return "movie/list";
    }

    @GetMapping("/movies/{id}")
    public String getMovieDetail(@PathVariable Integer id, Model model) {
        // ✅ 1. 누적 조회 기록 저장
        movieViewService.recordView(id, null); // 로그인 연동 전: userId = null

        // ✅ 2. 일일 조회수 +1 누적
        movieViewDailyService.increaseDailyViewCount(id);

        Optional<MovieCache> movieOpt = movieService.findMovieEntityById(id);

        if (movieOpt.isPresent()) {
            MovieCache movie = movieOpt.get();
            MovieResponseDto movieDto = MovieResponseDto.fromEntity(movie);
            MovieDetailDto detail = movieService.getMovieDetailFromTmdb(movie.getMovieId());

            if (detail != null && detail.isAdult()) {
                model.addAttribute("adultRestricted", true);
                return "movie/detail";
            }

            model.addAttribute("movie", movieDto);
            model.addAttribute("detail", detail);
            model.addAttribute("adultRestricted", false);
            return "movie/detail";

        } else {
            MovieDetailDto detail = movieService.getMovieDetailFromTmdbId(id);

            if (detail != null && detail.isAdult()) {
                model.addAttribute("adultRestricted", true);
                return "movie/detail";
            }

            movieCacheService.saveIfNotExists(detail);

            model.addAttribute("detail", detail);
            model.addAttribute("adultRestricted", false);
            return "movie/detail";
        }
    }

    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam String keyword,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {
        Page<MovieSearchResultDto> results = movieService.searchMoviesFromTmdb(keyword, page);
        int resultCount = movieService.getSearchResultCount(keyword);

        model.addAttribute("movies", results.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("resultCount", resultCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", results.getTotalPages());
        return "movie/list";
    }
}
