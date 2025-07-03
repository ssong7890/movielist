package com.sec.movietalk.actor.dto;

import java.util.List;

public class TmdbActorSearchResponse {
    private List<ActorDto> results; //TMDB API의 응답 JSON 중 "results"라는 키에 해당하는 값을 받을 필드

    // 외부에서 이 클래스의 results 값을 조회하기 위한 getter 메서드
    public List<ActorDto> getResults() {
        return results;
    }

    // JSON 파싱할 때 Jackson이 내부적으로 호출해서 데이터를 채워주는 setter 메서드
    public void setResults(List<ActorDto> results) {
        this.results = results;
    }
}
