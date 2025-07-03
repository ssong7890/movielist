package com.sec.movietalk.movie.mapper;

import java.util.Map;

public class GenreMapper {

    private static final Map<String, String> genreMap = Map.ofEntries(
            Map.entry("액션", "28"),
            Map.entry("모험", "12"),
            Map.entry("애니메이션", "16"),
            Map.entry("코미디", "35"),
            Map.entry("범죄", "80"),
            Map.entry("다큐멘터리", "99"),
            Map.entry("드라마", "18"),
            Map.entry("가족", "10751"),
            Map.entry("판타지", "14"),
            Map.entry("역사", "36"),
            Map.entry("공포", "27"),
            Map.entry("음악", "10402"),
            Map.entry("미스터리", "9648"),
            Map.entry("로맨스", "10749"),
            Map.entry("SF", "878"),
            Map.entry("TV 영화", "10770"),
            Map.entry("스릴러", "53"),
            Map.entry("전쟁", "10752"),
            Map.entry("서부", "37")
    );

    /**
     * 한글 장르명 → TMDB 장르 ID 변환
     * @param name 한글 장르명 (예: 액션, 드라마)
     * @return TMDB 장르 ID (예: "28") / null 반환 가능
     */
    public static String getGenreId(String name) {
        return genreMap.getOrDefault(name.trim(), null);
    }
}
