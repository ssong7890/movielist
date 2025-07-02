package com.sec.movietalk.movie.service;

import com.sec.movietalk.common.domain.movie.MovieViewDaily;
import com.sec.movietalk.common.domain.movie.MovieViewDailyId;
import com.sec.movietalk.movie.repository.MovieViewDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MovieViewDailyService {

    private final MovieViewDailyRepository movieViewDailyRepository;

    /**
     * 일일 조회수 1 증가 (해당 날짜 + 영화 ID 기준)
     */
    @Transactional
    public void increaseDailyViewCount(Integer movieId) {
        LocalDate today = LocalDate.now();
        MovieViewDailyId id = new MovieViewDailyId(movieId, today);

        movieViewDailyRepository.findById(id).ifPresentOrElse(
                record -> {
                    record.setCnt(record.getCnt() + 1); // ✅ 기존 레코드 +1
                },
                () -> {
                    // ✅ builder 대신 생성자 직접 호출
                    MovieViewDaily newRecord = new MovieViewDaily(movieId, today, 1);
                    movieViewDailyRepository.save(newRecord);
                }
        );
    }

    /**
     * 특정 영화의 오늘 기준 일일 조회수 반환 (없으면 0)
     */
    @Transactional(readOnly = true)
    public int getTodayViewCount(Integer movieId) {
        return movieViewDailyRepository
                .findByMovieIdAndViewDate(movieId, LocalDate.now())
                .map(MovieViewDaily::getCnt) // ✅ 필드명에 맞춰 getCnt()로 수정
                .orElse(0);
    }
}
