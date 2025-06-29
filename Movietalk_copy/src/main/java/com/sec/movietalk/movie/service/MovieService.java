package com.sec.movietalk.movie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sec.movietalk.client.TmdbClient;
import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.movie.dto.MovieDetailDto;
import com.sec.movietalk.movie.dto.MovieResponseDto;
import com.sec.movietalk.movie.dto.MovieSearchResultDto;
import com.sec.movietalk.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TmdbClient tmdbClient;

    public List<MovieResponseDto> getAllMoviesSortedByReleaseDate() {
        return movieRepository.findAllByOrderByReleaseDateDesc().stream()
                .map(MovieResponseDto::fromEntity)
                .toList();
    }

    public MovieResponseDto getMovieById(Long id) {
        MovieCache movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다"));
        return MovieResponseDto.fromEntity(movie);
    }

    public Optional<MovieCache> findMovieEntityById(Long id) {
        return movieRepository.findById(id);
    }

    // ✅ 내부 DB에서 TMDB 상세정보 가져오는 경우
    public MovieDetailDto getMovieDetailFromTmdb(Long id) {
        MovieCache movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다"));
        Integer tmdbId = movie.getMovieId();

        String url = "https://api.themoviedb.org/3/movie/" + tmdbId +
                "?api_key=" + tmdbClient.getApiKey() + "&language=ko-KR";

        RestTemplate restTemplate = new RestTemplate();
        MovieDetailDto detail = restTemplate.getForObject(url, MovieDetailDto.class);

        // ✅ 성인 여부 체크
        if (detail != null && detail.isAdult()) {
            detail.setRestricted(true);
        }

        return detail;
    }

    // ✅ 내부 DB가 없을 때 외부 API 직접 호출
    public MovieDetailDto getMovieDetailFromTmdbId(Long tmdbId) {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbId +
                "?api_key=" + tmdbClient.getApiKey() + "&language=ko-KR";

        RestTemplate restTemplate = new RestTemplate();
        MovieDetailDto detail = restTemplate.getForObject(url, MovieDetailDto.class);

        // ✅ 성인 여부 체크
        if (detail != null && detail.isAdult()) {
            detail.setRestricted(true);
        }

        return detail;
    }

    public List<MovieSearchResultDto> searchMoviesFromTmdb(String keyword) {
        String encodedKeyword = UriUtils.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + tmdbClient.getApiKey()
                + "&language=ko-KR&query=" + encodedKeyword;

        RestTemplate restTemplate = new RestTemplate();
        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        JsonNode results = response.path("results");

        List<MovieSearchResultDto> searchResults = new ArrayList<>();

        for (JsonNode result : results) {
            String title = result.path("title").asText();
            String overview = result.path("overview").asText();
            String posterPath = result.path("poster_path").asText(null);
            String releaseDate = result.path("release_date").asText(null);

            String posterUrl = posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : null;

            MovieSearchResultDto dto = new MovieSearchResultDto(title, overview, posterPath, releaseDate, posterUrl);
            searchResults.add(dto);
        }

        return searchResults;
    }
}
