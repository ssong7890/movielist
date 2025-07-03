package com.sec.movietalk.popular.service;

import com.sec.movietalk.common.util.RankingPeriod;
import com.sec.movietalk.common.util.SortType;
import com.sec.movietalk.popular.dto.*;
import com.sec.movietalk.popular.repository.MovieViewAggregationRepository;
import com.sec.movietalk.popular.repository.ReviewAggregationRepository;
import com.sec.movietalk.popular.repository.projection.ReviewCountProjection;
import com.sec.movietalk.popular.repository.projection.ViewCountProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopularMovieService {

    private final ReviewAggregationRepository     reviewRepo;
    private final MovieViewAggregationRepository  viewRepo;
    private final MovieMetaService                metaService;

    /** 인기 영화 TOP 리스트 (조회수 / 리뷰수) */
    public PopularMoviePageResponse getPopularMovies(PopularMoviePageRequest req) {

        // 1) 정렬 기준 & 기간 기본값 처리
        SortType       sort   = req.getSort()   != null ? req.getSort()   : SortType.VIEWS;
        RankingPeriod  period = req.getPeriod() != null ? req.getPeriod() : RankingPeriod.DAILY;

        // 2) 페이징 객체 생성 (0-based page, size)
        PageRequest pageable = PageRequest.of(req.getPage(), req.getSize());

        // 3) 랭킹 기간(from 시점) 계산
        // ALLTIME인 경우부터는 from을 쓰지 않음
        LocalDateTime from = (period == RankingPeriod.ALLTIME
                       ? null
                       : LocalDateTime.now().minusDays(period.getDays()));

        // 4) 정렬 기준에 따라 리뷰 집계 또는 조회 집계 호출
        Page<?> rawPage;
           if (sort == SortType.REVIEWS) {
                   rawPage = (period == RankingPeriod.ALLTIME)
                               ? reviewRepo.findTopMoviesByReviewCountAllTime(pageable)
                               : reviewRepo.findTopMoviesByReviewCount(from, pageable);
               } else {
                   rawPage = (period == RankingPeriod.ALLTIME)
                               ? viewRepo.findTopMoviesByViewCountAllTime(pageable)
                               : viewRepo.findTopMoviesByViewCount(from, pageable);
               }

        // 5) 전역 순위 계산을 위한 시작 번호
        int startRank = pageable.getPageNumber() * pageable.getPageSize() + 1;

        // 6) 집계 결과(Projection) → 카드 DTO 변환
        List<PopularMovieCardDto> cards = new ArrayList<>();

        int idx = 0;
        for (Object p : rawPage) {
            int movieId;
            long cnt;
            if (p instanceof ReviewCountProjection pr) {
                movieId = pr.getMovieId();
                cnt     = pr.getCnt();
            } else if (p instanceof ViewCountProjection pv) {
                movieId = pv.getMovieId();
                cnt     = pv.getCnt();
            } else {
                continue;
            }
            cards.add(toDto(startRank + idx++, movieId, cnt, sort));
        }

        // 7) 페이징 메타 정보 구성
        PageMeta meta = PageMeta.builder()
                .page(rawPage.getNumber())
                .size(rawPage.getSize())
                .totalElements(rawPage.getTotalElements())
                .totalPages(rawPage.getTotalPages())
                .build();

        // 8) 응답 DTO 조립 및 반환
        return PopularMoviePageResponse.builder()
                .content(cards)
                .meta(meta)
                .build();
    }

    /** Projection → 카드 DTO 변환 */
    private PopularMovieCardDto toDto(int rank, int movieId, long cnt, SortType sort) {

        // 1) MovieCache 조회/저장
        var movie = metaService.getOrFetchMovie(movieId);

        // 2) 상위 배우 목록 ActorChip 생성
        var actors = metaService.getTopActorChips(movieId, 3);

        // 3) 라벨 결정 ("리뷰수" or "조회수")
        String metricLabel = (sort == SortType.REVIEWS) ? "리뷰수" : "조회수";

        // 4) DTO 빌드
        return PopularMovieCardDto.builder()
                .rank(rank)
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .posterUrl(movie.getPosterUrl())
                .releaseDate(movie.getReleaseDate())
                .metricLabel(metricLabel)
                .metricValue(cnt)
                .actorChips(actors)
                .build();
    }
}
