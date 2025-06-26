package com.sec.movietalk.movie.service;

import com.sec.movietalk.movie.dto.MovieResponseDto;
import com.sec.movietalk.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieResponseDto> getAllMoviesSortedByReleaseDate() {
        return movieRepository.findAllByOrderByReleaseDateDesc().stream()
                .map(MovieResponseDto::fromEntity)
                .toList();
    }
}
