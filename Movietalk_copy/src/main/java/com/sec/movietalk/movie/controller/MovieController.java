package com.sec.movietalk.movie.controller;

import com.sec.movietalk.movie.dto.MovieDetailDto;
import com.sec.movietalk.movie.dto.MovieResponseDto;
import com.sec.movietalk.movie.dto.MovieSearchResultDto;
import com.sec.movietalk.movie.service.MovieService;
import com.sec.movietalk.client.TmdbClient;
import com.sec.movietalk.common.domain.movie.MovieCache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final TmdbClient tmdbClient;

    @GetMapping("/movies")
    public String showMovieList(Model model) {
        List<MovieResponseDto> movies = movieService.getAllMoviesSortedByReleaseDate();
        model.addAttribute("movies", movies);
        return "movie/list";
    }

    @GetMapping("/movies/{id}")
    public String getMovieDetail(@PathVariable Long id, Model model) {
        Optional<MovieCache> movieOpt = movieService.findMovieEntityById(id);

        if (movieOpt.isPresent()) {
            MovieCache movie = movieOpt.get();
            MovieResponseDto movieDto = MovieResponseDto.fromEntity(movie);
            MovieDetailDto detail = movieService.getMovieDetailFromTmdb(Long.valueOf(movie.getMovieId()));

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

            model.addAttribute("detail", detail);
            model.addAttribute("adultRestricted", false);
            return "movie/detail";
        }
    }

    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam String keyword, Model model) {
        List<MovieSearchResultDto> results = movieService.searchMoviesFromTmdb(keyword);
        model.addAttribute("movies", results);
        model.addAttribute("keyword", keyword);
        return "movie/list";
    }
}
