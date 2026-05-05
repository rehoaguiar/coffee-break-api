package com.rehoaguiar.coffeebreakapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class CoffeebreakapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeebreakapiApplication.class, args);
	}

}
