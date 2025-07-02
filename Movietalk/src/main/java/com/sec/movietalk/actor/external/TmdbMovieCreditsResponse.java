package com.sec.movietalk.actor.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Getter
@NoArgsConstructor
public class TmdbMovieCreditsResponse {

    private List<Cast> cast;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Cast {
        private String title;
        private String character;

        public LocalDate getParsedReleaseDate() {
            try {
                return releaseDate != null ? LocalDate.parse(releaseDate) : null;
            } catch (DateTimeParseException e) {
                return null;
            }
        }

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("overview")
        private String overview;

        @JsonProperty("vote_average")
        private Double voteAverage;

    }
}
