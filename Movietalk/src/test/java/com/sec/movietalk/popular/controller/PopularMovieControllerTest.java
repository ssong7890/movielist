package com.sec.movietalk.popular.controller;

import com.sec.movietalk.common.util.RankingPeriod;
import com.sec.movietalk.common.util.SortType;
import com.sec.movietalk.popular.dto.ActorChip;
import com.sec.movietalk.popular.dto.PageMeta;
import com.sec.movietalk.popular.dto.PopularMovieCardDto;
import com.sec.movietalk.popular.dto.PopularMoviePageResponse;
import com.sec.movietalk.popular.service.PopularMovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("deprecation")
@WithMockUser   // 인증된 사용자가 테스트를 실행하도록 설정
@WebMvcTest(PopularMovieController.class)  // PopularMovieController만 로드하여 MVC 테스트 실행
class PopularMovieControllerTest {

    @Autowired
    private MockMvc mvc;  // MockMvc를 통해 HTTP 요청/응답 검증

    @MockBean
    private PopularMovieService service;  // 서비스 레이어를 Mock으로 대체

    /**
     * 테스트용 더미 응답 객체 생성 헬퍼 메서드
     * @param metricLabel "조회수" 또는 "리뷰수"
     * @param metricValue 조회수/리뷰수 값
     * @return PopularMoviePageResponse 더미 응답 객체
     */
    private PopularMoviePageResponse sampleResponse(String metricLabel, long metricValue) {
        // 1) 인기 영화 카드 DTO 생성
        PopularMovieCardDto card = PopularMovieCardDto.builder()
                .rank(1)  // 순위
                .movieId(123)
                .title("테스트 영화")
                .posterUrl("/p.jpg")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .metricLabel(metricLabel)  // "조회수" 또는 "리뷰수"
                .metricValue(metricValue)
                .actorChips(List.of(
                        ActorChip.builder().actorId(1).name("배우A").build(),
                        ActorChip.builder().actorId(2).name("배우B").build()
                ))
                .build();

        // 2) 페이징 메타 데이터 생성
        PageMeta meta = PageMeta.builder()
                .page(0)           // 페이지 인덱스
                .size(20)          // 페이지 크기
                .totalElements(1)  // 전체 요소 수
                .totalPages(1)     // 전체 페이지 수
                .build();

        // 3) 최종 응답 조립
        return PopularMoviePageResponse.builder()
                .content(List.of(card))
                .meta(meta)
                .build();
    }

    @Test
    @DisplayName("파라미터 없이 호출 시 기본값(조회수·일간)으로 응답 확인")
    void defaultShouldBeViews() throws Exception {
        // 1) 서비스가 기본 요청(VIEWS, DAILY, page=0, size=20) 받으면 '조회수=50' 응답을 반환하도록 설정
        given(service.getPopularMovies(argThat(req ->
                req.getSort()   == SortType.VIEWS &&
                        req.getPeriod() == RankingPeriod.DAILY &&
                        req.getPage()   == 0 &&
                        req.getSize()   == 20
        ))).willReturn(sampleResponse("조회수", 50L));

        // 2) GET /api/popular 요청 실행 및 응답 검증
        mvc.perform(get("/api/popular")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // HTTP 200 OK
                .andExpect(jsonPath("$.content[0].metricLabel").value("조회수"))  // metricLabel 검증
                .andExpect(jsonPath("$.content[0].metricValue").value(50));      // metricValue 검증
    }

    @Test
    @DisplayName("sort=REVIEWS, period=WEEKLY 파라미터로 호출 시 리뷰수 기준 응답 확인")
    void explicitReviewsShouldBeReviews() throws Exception {
        // 1) 서비스가 리뷰 요청(REVIEWS, WEEKLY) 받으면 '리뷰수=7' 응답을 반환하도록 설정
        given(service.getPopularMovies(argThat(req ->
                req.getSort()   == SortType.REVIEWS &&
                        req.getPeriod() == RankingPeriod.WEEKLY
        ))).willReturn(sampleResponse("리뷰수", 7L));

        // 2) GET /api/popular?sort=REVIEWS&period=WEEKLY 요청 실행 및 응답 검증
        mvc.perform(get("/api/popular")
                        .param("sort",   "REVIEWS")
                        .param("period", "WEEKLY")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].metricLabel").value("리뷰수"))
                .andExpect(jsonPath("$.content[0].metricValue").value(7));
    }

    @Test
    @DisplayName("잘못된 enum 파라미터(sort=invalid)로 호출 시 400 Bad Request 처리")
    void invalidEnumShouldReturn400() throws Exception {
        // GET /api/popular?sort=invalid 요청 실행 및 400 응답 검증
        mvc.perform(get("/api/popular")
                        .param("sort", "invalid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
