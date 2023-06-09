package com.example.adsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AdSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdSearchApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {

		return new RestTemplate();
	}

}
