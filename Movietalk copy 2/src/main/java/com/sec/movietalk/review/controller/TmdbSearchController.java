package com.sec.movietalk.review.controller;

import com.sec.movietalk.movie.dto.MovieSearchResultDto;
import com.sec.movietalk.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TmdbSearchController {

    private final MovieService movieService;

    @GetMapping("/api/tmdb/search")
    public List<Map<String, Object>> searchMovies(@RequestParam String query) {
        List<MovieSearchResultDto> results = movieService.searchMoviesFromTmdb(query, 0).getContent();

        return results.stream()
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", dto.getId());
                    map.put("title", dto.getTitle());
                    map.put("release_date", dto.getReleaseDate());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
