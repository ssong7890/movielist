package com.sec.movietalk.popular.client;

import com.sec.movietalk.client.TmdbClient;            // 기존 클래스 그대로 사용
import com.sec.movietalk.popular.dto.external.TmdbCreditsDto;
import com.sec.movietalk.popular.dto.external.TmdbMovieDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * TMDB 호출 전담(인기 영화 모듈 전용)
 */
@Component
@RequiredArgsConstructor
public class TmdbApiClient {

    private final RestTemplate rest;   // RestTemplateConfig 빈 주입, HTTP 클라이언트
    private final TmdbClient   tmdbClient; // 기존 클래스에서 API Key만 가져옴

    //MDB(The Movie Database) 버전 3 REST API의 기본 주소(base URL)
    private static final String BASE = "https://api.themoviedb.org/3";

    /** 영화 상세 정보 */
    public TmdbMovieDto fetchMovie(int movieId) {
        String uri = UriComponentsBuilder.fromHttpUrl(BASE)
                .path("/movie/{id}") //movieId 변수 값 삽입
                .queryParam("language", "ko-KR")
                .queryParam("api_key", tmdbClient.getApiKey()) //API Key 주입
                .build(movieId)
                .toString();

        return rest.getForObject(uri, TmdbMovieDto.class); //HTTP GET 요청을 url에 보냄, 서버가 돌려준 JSON 응답을 역직렬화
    }

    /** 출연진(credits) 정보 */
    public TmdbCreditsDto fetchCredits(int movieId) {
        String uri = UriComponentsBuilder.fromHttpUrl(BASE)
                .path("/movie/{id}/credits")
                .queryParam("language", "ko-KR")
                .queryParam("api_key", tmdbClient.getApiKey())
                .build(movieId)
                .toString();

        return rest.getForObject(uri, TmdbCreditsDto.class);
    }
}
