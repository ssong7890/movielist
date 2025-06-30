package com.sec.movietalk.movie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sec.movietalk.client.TmdbClient;
import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.movie.dto.MovieDetailDto;
import com.sec.movietalk.movie.dto.MovieResponseDto;
import com.sec.movietalk.movie.dto.MovieSearchResultDto;
import com.sec.movietalk.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TmdbClient tmdbClient;

    private static final int PAGE_SIZE = 20;

    public Page<MovieResponseDto> getPagedMovies(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("releaseDate").descending());
        Page<MovieCache> moviePage = movieRepository.findAll(pageable);
        List<MovieResponseDto> dtoList = moviePage.stream()
                .map(MovieResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, moviePage.getTotalElements());
    }

    public MovieResponseDto getMovieById(Long id) {
        MovieCache movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다"));
        return MovieResponseDto.fromEntity(movie);
    }

    public Optional<MovieCache> findMovieEntityById(Long id) {
        return movieRepository.findById(id);
    }

    public MovieDetailDto getMovieDetailFromTmdb(Long id) {
        MovieCache movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다"));
        Integer tmdbId = movie.getMovieId();

        String url = "https://api.themoviedb.org/3/movie/" + tmdbId +
                "?api_key=" + tmdbClient.getApiKey() + "&language=ko-KR";

        RestTemplate restTemplate = new RestTemplate();
        MovieDetailDto detail = restTemplate.getForObject(url, MovieDetailDto.class);

        if (detail != null && detail.isAdult()) {
            detail.setRestricted(true);
        }

        return detail;
    }

    public MovieDetailDto getMovieDetailFromTmdbId(Long tmdbId) {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbId +
                "?api_key=" + tmdbClient.getApiKey() + "&language=ko-KR";

        RestTemplate restTemplate = new RestTemplate();
        MovieDetailDto detail = restTemplate.getForObject(url, MovieDetailDto.class);

        if (detail != null && detail.isAdult()) {
            detail.setRestricted(true);
        }

        return detail;
    }

    public Page<MovieSearchResultDto> searchMoviesFromTmdb(String keyword, int page) {
        String encodedKeyword = UriUtils.encode(keyword, StandardCharsets.UTF_8);
        int tmdbPage = page + 1;
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + tmdbClient.getApiKey()
                + "&language=ko-KR"
                + "&include_adult=false"
                + "&page=" + tmdbPage
                + "&query=" + encodedKeyword;

        RestTemplate restTemplate = new RestTemplate();
        List<MovieSearchResultDto> searchResults = new ArrayList<>();

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            JsonNode results = response.path("results");
            int total = response.path("total_results").asInt(0);

            for (JsonNode result : results) {
                boolean adult = result.path("adult").asBoolean(false);
                String overview = result.path("overview").asText("").toLowerCase();

                if (adult || overview.contains("nudity") || overview.contains("sexual")
                        || overview.contains("성인") || overview.contains("19금")) {
                    continue; // 성인 영화 또는 선정적 내용 필터링
                }

                Long id = result.path("id").asLong();
                String title = result.path("title").asText(null);
                if (title == null || title.isBlank()) continue;

                String posterPath = result.path("poster_path").asText(null);
                String releaseDate = result.path("release_date").asText(null);
                String posterUrl = posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : null;

                MovieSearchResultDto dto = new MovieSearchResultDto(id, title, overview, posterPath, releaseDate, posterUrl, adult);
                searchResults.add(dto);
            }

            int start = 0;
            int end = Math.min(PAGE_SIZE, searchResults.size());
            List<MovieSearchResultDto> pagedList = searchResults.subList(start, end);

            return new PageImpl<>(pagedList, PageRequest.of(page, PAGE_SIZE), total);

        } catch (RestClientException e) {
            log.error("TMDB 검색 API 호출 실패: {}", e.getMessage());
            return Page.empty();
        }
    }

    public int getSearchResultCount(String keyword) {
        String encodedKeyword = UriUtils.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + tmdbClient.getApiKey()
                + "&language=ko-KR"
                + "&include_adult=false"
                + "&page=1"
                + "&query=" + encodedKeyword;

        RestTemplate restTemplate = new RestTemplate();
        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            return response.path("total_results").asInt(0);
        } catch (RestClientException e) {
            log.error("TMDB 검색 수 조회 실패: {}", e.getMessage());
            return 0;
        }
    }
}
