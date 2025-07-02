package com.sec.movietalk.recommendation.controller;


import com.sec.movietalk.recommendation.dto.CheckFavoriteRes;
import com.sec.movietalk.recommendation.dto.OnboardingMovie;
import com.sec.movietalk.recommendation.dto.FavoriteSaveReq;
import com.sec.movietalk.recommendation.dto.MovieRecommendation;
import com.sec.movietalk.recommendation.dto.RecommendationRes;
import com.sec.movietalk.recommendation.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    // 1: 선호 영화 => 3인지 확인
    @GetMapping("/check/{user_id}")
    public CheckFavoriteRes checkFavorite(@PathVariable("user_id") Long userId) {
        return service.hasFavorite(userId);
    }

    // 2 온보딩용 후보 영화 20편
    @GetMapping("/onboarding/movies")
    public List<OnboardingMovie> getOnboardingMovies() {
        return service.getOnboardingMovies();
    }

    // 3: 온보딩 완료 → 선호 영화 저장
    @PostMapping("/onboarding/complete/{user_id}")
    public void saveFavorites(@PathVariable("user_id") Long userId,
                              @RequestBody Map<String,List<Long>> body) {
        service.saveFavorites(userId, body.get("movie_ids"));
    }
    @GetMapping("/{user_id}")
    public List<MovieRecommendation> getRecs(@PathVariable("user_id") Long userId) {
        return service.getRecommendations(userId);
    }


}
