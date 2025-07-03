// dto/CheckFavoriteRes.java
package com.sec.movietalk.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

public class CheckFavoriteRes {
    private  boolean has_favorite;   // user_favorite ≥ 3 여부

    public CheckFavoriteRes(){}
    public CheckFavoriteRes(boolean has_favorite) {
        this.has_favorite = has_favorite;
    }
}
