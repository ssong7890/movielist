package com.sec.movietalk.recommendation.service;

import com.sec.movietalk.client.TmdbClient;
import com.sec.movietalk.common.domain.movie.MovieCache;
import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.common.domain.user.UserFavorite;
import com.sec.movietalk.recommendation.dto.CheckFavoriteRes;
import com.sec.movietalk.recommendation.dto.MovieRecommendation;
import com.sec.movietalk.recommendation.dto.OnboardingMovie;
import com.sec.movietalk.recommendation.repository.MovieCacheRepository;
import com.sec.movietalk.recommendation.repository.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final TmdbClient tmdbClient;
    private final MovieCacheRepository movieCacheRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    private static final int MAX_ONBOARDING = 100;
    /*
        1. 유저의 선호 영화 >= 3 확인
     */
    public CheckFavoriteRes hasFavorite(Long userID){
        long count = userFavoriteRepository.countByUser_UserId(userID);
        boolean has_favorite = (count >= 3);
        return new CheckFavoriteRes(has_favorite);
    }



    /*
     * 온보딩용 후보 영화 20편 반환
     * 기준:
     *  1) 주간 트렌딩      /trending/movie/week
     *  2) 인기            /movie/popular
     *  3) 평점 8.0 이상    /discover/movie?vote_average.gte=8
     *  4) 장르별 추천      /discover/movie?with_genres=X (액션, 코미디, 공포, 로맨스, 드라마)
     */
    public List<OnboardingMovie> getOnboardingMovies() {
        Set<OnboardingMovie> set = new LinkedHashSet<>();
        // 1. 주간 트렌딩
        set.addAll(tmdbClient.fetchTrendingMovies("week"));
        // 2) 인기 영화
        set.addAll(tmdbClient.fetchPopularMovies(1));
        // 3) 평점 8.0 이상
        set.addAll(tmdbClient.discoverMovies(Map.of("vote_average.gte", "8")));
        // 4) 장르별 2편씩
        int[] genres = {28, 35, 27, 10749, 18};
        for (int g : genres) {
            tmdbClient.discoverMovies(Map.of("with_genres", String.valueOf(g)))
                    .stream().limit(2)
                    .forEach(set::add);
        }


        return set.stream().limit(MAX_ONBOARDING).toList();

    }

   //    ------- 3. 선택된 영화들 UserFavorite에 저장 ------
   public void saveFavorites(Long userId, List<Long> movieIds) {
       User userRef = new User(userId);
       List<UserFavorite> list = movieIds.stream()
               .filter(id -> !userFavoriteRepository
                       .existsByUser_UserIdAndMovieId(userId, id.intValue()))
               .map(id -> new UserFavorite(userRef, id.intValue()))
               .collect(Collectors.toList());
       userFavoriteRepository.saveAll(list);
   }


    /*
     * 4. TMDB 추천 합집합 생성 + 필터링 + 정렬
     *    즐겨찾기된 영화별 추천 리스트를 합집합으로 취합하여
     *    중복·기존 즐겨찾기 제외 → 인기순 정렬 → 최대 20개 반환
     */
    public List<MovieRecommendation> getRecommendations(Long userId) {
        List<Integer> favIds = userFavoriteRepository.findAllByUser_UserId(userId)
                .stream().map(UserFavorite::getMovieId).toList();

        return favIds.stream()
                .flatMap(id -> tmdbClient.fetchRecommendations(id).stream())
                .filter(r -> !favIds.contains(r.getMovie_id().intValue())) // 기존 즐겨찾기 제외
                .collect(Collectors.toMap( // 중복 제거(popularity 최대값 유지)
                        MovieRecommendation::getMovie_id,
                        r -> r,
                        (r1,r2) -> r1.getPopularity() >= r2.getPopularity() ? r1 : r2))
                .values().stream()
                .sorted(Comparator.comparing(MovieRecommendation::getPopularity).reversed())
                .limit(20)
                .toList();
    }

}
