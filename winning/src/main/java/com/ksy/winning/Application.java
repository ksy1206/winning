package com.ksy.winning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ksy.winning")
@MapperScan("com.ksy.winning")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
