package com.sec.movietalk.client;


import com.sec.movietalk.actor.dto.ActorDto;
import com.sec.movietalk.actor.dto.TmdbActorSearchResponse;
import com.sec.movietalk.actor.external.TmdbMovieCreditsResponse;
import com.sec.movietalk.recommendation.dto.MovieRecommendation; // G1
import com.sec.movietalk.recommendation.dto.MovieRecommendation; // G1
import com.sec.movietalk.recommendation.dto.OnboardingMovie; // G1
import com.sec.movietalk.recommendation.dto.tmdb.TmdbMovie;//G1
import com.sec.movietalk.recommendation.dto.tmdb.TmdbMovieListResponse;//G1
import com.sec.movietalk.recommendation.dto.OnboardingMovie; // G1
import java.util.Collections; // G1
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UriComponentsBuilder; //G1
import java.nio.charset.StandardCharsets;

import java.util.*; //G1
import java.util.stream.Collectors; //G1


@Component
@Getter
public class TmdbClient {

    @Value("${tmdb.api-key}")
    private String apiKey;


    @PostConstruct
    public void init() {
        System.out.println("TMDB API Key = " + apiKey);
    }



    private final RestTemplate restTemplate = new RestTemplate();

    public List<ActorDto> searchActors(String query) {
        String url = "https://api.themoviedb.org/3/search/person?api_key=" + apiKey +
                "&language=ko-KR&query=" + UriUtils.encode(query, StandardCharsets.UTF_8);

        ResponseEntity<TmdbActorSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().getResults();
    }

    public ActorDto getActorDetail(int id) {
        String url = "https://api.themoviedb.org/3/person/" + id + "?api_key=" + apiKey + "&language=ko-KR";

        ResponseEntity<ActorDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ActorDto>() {}
        );

        return response.getBody();
    }

    public TmdbMovieCreditsResponse getMovieCredits(Long personId, String apiKey) {
        String url = "https://api.themoviedb.org/3/person/" + personId + "/movie_credits?api_key=" + apiKey + "&language=ko-KR";

        ResponseEntity<TmdbMovieCreditsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                TmdbMovieCreditsResponse.class
        );

        return response.getBody();
    }

    // -------------------G1-------------------//
    /** ➊ 주간 트렌딩 (<code>/trending/movie/week</code>) */
    public List<OnboardingMovie> fetchTrendingMovies(String timeWindow) {
        String url = String.format(
                "https://api.themoviedb.org/3/trending/movie/%s?api_key=%s&language=ko-KR",
                timeWindow, apiKey
        );
        ResponseEntity<TmdbMovieListResponse> resp = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<TmdbMovieListResponse>(){}
        );
        return resp.getBody().getResults().stream()
                .map(m -> OnboardingMovie.builder()
                        .movieId(m.getId())
                        .title(m.getTitle())
                        .posterUrl("https://image.tmdb.org/t/p/w300"+m.getPoster_path())
                        .build())
                .collect(Collectors.toList());
    }

    /** ➋ 인기 영화 (<code>/movie/popular?page=X</code>) */
    public List<OnboardingMovie> fetchPopularMovies(int page) {
        String url = String.format(
                "https://api.themoviedb.org/3/movie/popular?api_key=%s&language=ko-KR&page=%d",
                apiKey, page
        );
        ResponseEntity<TmdbMovieListResponse> resp = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<TmdbMovieListResponse>(){}
        );
        return resp.getBody().getResults().stream()
                .map(m -> OnboardingMovie.builder()
                        .movieId(m.getId())
                        .title(m.getTitle())
                        .posterUrl("https://image.tmdb.org/t/p/w300"+m.getPoster_path())
                        .build())
                .collect(Collectors.toList());
    }
    /** ➌ Discover 공통 (<code>/discover/movie?…</code>) */
    public List<OnboardingMovie> discoverMovies(Map<String,String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://api.themoviedb.org/3/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("language", "ko-KR");
        params.forEach(builder::queryParam);

        ResponseEntity<TmdbMovieListResponse> resp = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<TmdbMovieListResponse>(){}
        );
        return resp.getBody().getResults().stream()
                .map(m -> OnboardingMovie.builder()
                        .movieId(m.getId())
                        .title(m.getTitle())
                        .posterUrl("https://image.tmdb.org/t/p/w300"+m.getPoster_path())
                        .build())
                .collect(Collectors.toList());
    }

    /** ➍ 영화별 추천 (<code>/movie/{id}/recommendations</code>) */
    public List<MovieRecommendation> fetchRecommendations(Integer movieId) {
        String url = String.format(
                "https://api.themoviedb.org/3/movie/%d/recommendations?api_key=%s&language=ko-KR",
                movieId, apiKey
        );
        ResponseEntity<TmdbMovieListResponse> resp = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<TmdbMovieListResponse>(){}
        );
        return resp.getBody().getResults().stream()
                .map(m -> MovieRecommendation.builder()
                        .movie_id((long)m.getId())
                        .title(m.getTitle())
                        .poster_path("https://image.tmdb.org/t/p/w300"+m.getPoster_path())
                        .vote_average(m.getVote_average())
                        .popularity(m.getPopularity())
                        .build())
                .collect(Collectors.toList());
    }




    // -------------------G1-------------------//


}


