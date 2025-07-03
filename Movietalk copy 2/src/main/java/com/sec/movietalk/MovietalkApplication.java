package com.sec.movietalk;

import com.sec.movietalk.client.TmdbClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovietalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovietalkApplication.class, args);
	}
}



