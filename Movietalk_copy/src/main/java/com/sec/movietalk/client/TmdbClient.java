package com.sec.movietalk.client;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class TmdbClient {

    @Value("${tmdb.api-key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        System.out.println("TMDB API Key = " + apiKey);
    }


}


