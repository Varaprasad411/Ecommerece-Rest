package com.jsp.Ecommerece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EcommereceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommereceApplication.class, args);
	}

}
