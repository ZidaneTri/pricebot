package com.example.pricebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@Configuration
//@EnableScheduling
public class PricebotApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(PricebotApplication.class, args);


	}

}
