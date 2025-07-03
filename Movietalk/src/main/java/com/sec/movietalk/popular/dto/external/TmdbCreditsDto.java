package com.sec.movietalk.popular.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbCreditsDto {
    private int id;
    private List<Cast> cast; //해당 영화의 배우 목록을 담고 있는 리스트

    @Data
    public static class Cast {
        private int    id; //TMDB상 배우의 고유 ID
        private String name; //배우 이름
        @JsonProperty("profile_path")
        private String profilePath; //프로필 이미지 경로
        private String character; //영화에서 연기한 역할 이름
    }
}