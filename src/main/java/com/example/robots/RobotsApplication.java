package com.example.robots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RobotsApplication {
	public static void main(String[] args) {
		SpringApplication.run(RobotsApplication.class, args);
	}
}