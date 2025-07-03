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
        private Long id;

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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }
    }
}
