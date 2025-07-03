package com.sec.movietalk.common.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

//TMDB API나 다른 REST 호출 중에 네트워크 지연이나 서버 장애가 발생해도
//지정한 시간 내에 실패를 감지하고 예외를 던짐으로써
//애플리케이션이 무한 대기에 빠지지 않도록 보호
@Configuration
public class RestTemplateConfig { //HTTP 타임아웃을 설정

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory
                = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3_000);   // 서버 연결 시도 시 최대 3초 대기
        factory.setReadTimeout(5_000); //서버로부터 응답 데이터 수신 시 최대 5초 대기

        return new RestTemplate(factory);
    }

}
