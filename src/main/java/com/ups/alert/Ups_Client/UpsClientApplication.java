package com.ups.alert.Ups_Client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UpsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpsClientApplication.class, args);
	}


}
