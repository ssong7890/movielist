package com.sec.movietalk.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 기간별 랭킹(일/주/월/전체) 계산용 Enum
 */
public enum RankingPeriod {

    DAILY(1),
    WEEKLY(7),
    MONTHLY(30),
    ALLTIME(Integer.MAX_VALUE);

    private final int days;

    RankingPeriod(int days) {
        this.days = days;
    }

    //해당 상수가 가진 일수를 리턴
    public int getDays() {
        return days;
    }

    //“지금(now)”을 기준으로, 과거로부터 며칠 전까지가 랭킹 계산 대상인지 시작 시점을 돌려줌
    public LocalDateTime getFrom(LocalDateTime now) {
        // ALLTIME(전체 기간)인 경우 → 가능한 최소값을 리턴해서 모든 기록을 포함
        //// 그 외(DAILY, WEEKLY, MONTHLY)인 경우 → now에서 days만큼 과거로 뺀 시점을 리턴
        return (this == ALLTIME) ? LocalDateTime.MIN : now.minusDays(days);
    }

    //문자열 파라미터를 받아, 대응되는 RankingPeriod 상수로 변환
    public static RankingPeriod of(String val) {
        if (val == null) return DAILY;
        try {
            return RankingPeriod.valueOf(val.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return DAILY;
        }
    }
}
