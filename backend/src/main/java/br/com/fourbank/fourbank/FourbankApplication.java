package br.com.fourbank.fourbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FourbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(FourbankApplication.class, args);
	}

}
