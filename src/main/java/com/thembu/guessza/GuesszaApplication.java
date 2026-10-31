package com.thembu.guessza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan //helps spring boot find api key property
public class GuesszaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuesszaApplication.class, args);
	}

}
