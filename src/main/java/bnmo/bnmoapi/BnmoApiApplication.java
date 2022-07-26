package bnmo.bnmoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BnmoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BnmoApiApplication.class, args);
	}

}
