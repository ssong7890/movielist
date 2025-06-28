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

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TmdbClient tmdbClient; // TMDb API 호출용 클라이언트 주입

    // 목록 영화 페이지용
    public List<MovieResponseDto> getAllMoviesSortedByReleaseDate() {
        return movieRepository.findAllByOrderByReleaseDateDesc().stream()
                .map(MovieResponseDto::fromEntity)
                .toList();
    }

    // 내부 DB에서 기본 영화 정보 조회
    public MovieResponseDto getMovieById(Long id) {
        MovieCache movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다"));
        return MovieResponseDto.fromEntity(movie);
    }

    // TMDb API에서 상세 영화 정보 조회
    public MovieDetailDto getMovieDetailFromTmdb(Long id) {
        MovieCache movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다"));
        Integer tmdbId = movie.getMovieId(); // 내부 DB에 있는 TMDb ID로 API 호출

        String url = "https://api.themoviedb.org/3/movie/" + tmdbId +
                "?api_key=" + tmdbClient.getApiKey() + "&language=ko-KR";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, MovieDetailDto.class);
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

                // ✅ posterUrl 조립
                String posterUrl = posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : null;

                // ✅ DTO 생성 후 리스트에 추가
                MovieSearchResultDto dto = new MovieSearchResultDto(title, overview, posterPath, releaseDate, posterUrl);
                searchResults.add(dto);
            }



        return searchResults;
    }


}
