package com.sec.movietalk.popular.scheduler;

import com.sec.movietalk.common.domain.movie.MovieViewDaily;
import com.sec.movietalk.common.domain.movie.MovieViewDailyId;
import com.sec.movietalk.popular.repository.MovieViewDailyRepository;
import com.sec.movietalk.popular.repository.MovieViewsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@EnableScheduling          // 한 곳만 선언돼 있으면 전체 앱에 적용
@RequiredArgsConstructor
public class MovieViewDailyBatchScheduler {

    private final EntityManager           em;         // 네이티브 집계용
    private final MovieViewDailyRepository dailyRepo;

    /**
     * 매일 새벽 02:00 전일 조회수를 집계하여 movie_view_daily 테이블에 INSERT
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void aggregateDailyViews() {

        LocalDate target = LocalDate.now().minusDays(1);

        // 네이티브 집계 쿼리: (movie_id , cnt)
        List<Tuple> rows = em.createNativeQuery("""
            SELECT movie_id   AS movieId,
                   COUNT(*)    AS cnt
            FROM   movie_views
            WHERE  DATE(viewed_at) = :target
            GROUP  BY movie_id
            """, Tuple.class)
                .setParameter("target", target)
                .getResultList();

        rows.forEach(t -> {
            int  movieId = (Integer) t.get("movieId");
            long cnt     = ((Number) t.get("cnt")).longValue();

            MovieViewDaily entity = new MovieViewDaily(
                    movieId,
                    target,
                    (int) cnt
            );
            dailyRepo.save(entity);     // PK(movieId, viewDate) → upsert 안 되면 try/catch
        });

        log.info("aggregated {} movie view rows for {}", rows.size(), target);
    }
}
