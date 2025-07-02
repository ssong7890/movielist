package com.sec.movietalk.recommendation.dto;

import lombok.Getter;
import java.util.List;

public class FavoriteSaveReq {
    private List<Long> movie_ids;   // 3편 이상 선택

    // Jackson이 바디를 바인딩하려면 기본 생성자가 필요합니다
    public FavoriteSaveReq() { }

    // 컨트롤러에서 사용하기 위한 getter
    public List<Long> getMovie_ids() {
        return movie_ids;
    }

    // JSON 바인딩을 위해 setter도 추가
    public void setMovie_ids(List<Long> movie_ids) {
        this.movie_ids = movie_ids;
    }
}