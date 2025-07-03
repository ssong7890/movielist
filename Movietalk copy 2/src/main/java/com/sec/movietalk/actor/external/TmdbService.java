package com.sec.movietalk.actor.external;

import com.sec.movietalk.actor.dto.FilmographyDto;
import com.sec.movietalk.actor.external.TmdbMovieCreditsResponse;
import com.sec.movietalk.client.TmdbClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TmdbService {

    private final TmdbClient tmdbClient;

    @Value("${tmdb.api-key}")
    private String apiKey;

    public List<FilmographyDto> getFilmographyByPersonId(Long personId) {
        TmdbMovieCreditsResponse response = tmdbClient.getMovieCredits(personId, apiKey);

        return response.getCast().stream()
                .filter(c -> c.getTitle() != null)
                .sorted(Comparator.comparing(
                        TmdbMovieCreditsResponse.Cast::getReleaseDate,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .map(c -> new FilmographyDto(
                        c.getId(),
                        c.getTitle(),
                        c.getCharacter(),
                        c.getReleaseDate(),
                        c.getPosterPath(),
                        c.getOverview(),
                        c.getVoteAverage()
                ))
                .collect(Collectors.toList());
    }
}
