package com.sec.movietalk.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * movieCache ‧ creditsCache 등 이름별 캐시를 관리
     * - 최대 5,000 엔트리
     * - 마지막 접근 기준 24h 후 만료
     */
    //지정한 이름의 캐시(예: "movieCache", "creditsCache", "trendingCache")를 통해
    // 첫 호출 시 데이터를 메모리에 저장해 두었다가, 이후 동일한 key로 조회하면 메모리에서 즉시 반환
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                "movieCache", "creditsCache", "trendingCache");
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(5_000) //캐시당 보관할 엔트리(키-값 쌍)의 최대 개수를 5,000개로 제한
                        .expireAfterAccess(Duration.ofHours(24)) //마지막으로 읽거나 쓴 시점을 기준으로 24시간이 지나면 해당 엔트리를 자동 만료
        );
        return manager;
    }
}
