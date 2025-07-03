package com.sec.movietalk.popular.service;

import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.popular.client.TmdbApiClient;
import com.sec.movietalk.popular.dto.ActorChip;
import com.sec.movietalk.popular.dto.external.TmdbMovieDto;
import com.sec.movietalk.popular.repository.MovieCache2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieMetaService {

    private final MovieCache2Repository cacheRepo;
    private final TmdbApiClient        tmdbClient;

    /** 캐시에 있으면 반환, 없으면 TMDB 호출 후 저장 */
    @Transactional
    public MovieCache getOrFetchMovie(int movieId) {
        //먼저 로컬 DB(movie_cache 테이블) 에 저장된 MovieCache 엔티티를 찾음
        return cacheRepo.findById(movieId)
                //없으면 TMDB API (TmdbApiClient.fetchMovie) 를 호출
                .orElseGet(() -> cacheRepo.save(convert(tmdbClient.fetchMovie(movieId))));
    }

    /** 상위 배우 칩 N개 반환 */
    public List<ActorChip> getTopActorChips(int movieId, int limit) {
        return tmdbClient.fetchCredits(movieId).getCast().stream()
                .limit(limit)
                .map(c -> ActorChip.builder()
                        .actorId(c.getId())
                        .name(c.getName())
                        .build())
                .collect(Collectors.toList());
    }

    //mdbMovieDto 객체의 필드 값을 꺼내서, 로컬 DB에 저장할 MovieCache 엔티티로 1:1 매핑
    private MovieCache convert(TmdbMovieDto dto) {
        return MovieCache.builder()
                .movieId(dto.getId())
                .title(dto.getTitle())
                .posterUrl(dto.getPosterPath())
                .overview(dto.getOverview())
                .releaseDate(dto.getReleaseDate())
                .build();
    }
}
