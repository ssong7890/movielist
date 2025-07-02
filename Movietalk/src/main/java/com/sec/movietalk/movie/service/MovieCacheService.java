package com.sec.movietalk.movie.service;

import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.movie.dto.MovieDetailDto;
import com.sec.movietalk.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MovieCacheService {

    private final MovieRepository movieRepository;

    public void saveIfNotExists(MovieDetailDto detailDto) {
        Integer movieId = detailDto.getTmdbId();

        if (!movieRepository.existsById(movieId)) {
            MovieCache entity = MovieCache.builder()
                    .movieId(movieId)
                    .title(detailDto.getTitle())
                    .posterUrl("https://image.tmdb.org/t/p/w500" + detailDto.getPosterPath())
                    .overview(detailDto.getOverview())
                    .releaseDate(LocalDate.parse(detailDto.getReleaseDate()))
                    .build();

            movieRepository.save(entity);
        }
    }
}
