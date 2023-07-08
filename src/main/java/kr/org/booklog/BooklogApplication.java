package kr.org.booklog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BooklogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooklogApplication.class, args);
	}

}
