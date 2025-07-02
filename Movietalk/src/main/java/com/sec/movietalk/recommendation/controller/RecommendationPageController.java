package com.sec.movietalk.recommendation.controller;

import com.sec.movietalk.recommendation.repository.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.sec.movietalk.common.util.UserUtil.extractUserId;

@Controller
@RequiredArgsConstructor
public class RecommendationPageController {

    private final UserFavoriteRepository userFavoriteRepository;

    /** /recommend 진입점 */
    @GetMapping("/recommend")
    public String recommendPage(Authentication authentication,
                                Model model) {
        Long user_id = extractUserId(authentication != null ? authentication.getPrincipal() : null);
        if (user_id == null) {
            // 비로그인 상태: 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }   // DB 컬럼명 그대로
        long favCnt  = userFavoriteRepository.countByUser_UserId(user_id);

        if (favCnt < 3) {
            // 즐겨찾기가 3개 미만이면 온보딩 페이지로 리다이렉트
            return "redirect:/onboarding";
        }

        // 3개 이상이면 추천 리스트 뷰로
        model.addAttribute("user_id", user_id);    // recommend/list.html 에서 JS로 쓸 값
        return "recommend/recommend";                   // templates/recommend/list.html
    }

    @GetMapping("/onboarding")
    public String onboardingPage(Authentication authentication, Model model) {
        Long userId = extractUserId(authentication != null ? authentication.getPrincipal() : null);

        model.addAttribute("user_id", userId);
        return "recommend/onboarding";
    }
}
