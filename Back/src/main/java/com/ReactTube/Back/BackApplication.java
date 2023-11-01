package com.ReactTube.Back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Function;

@SpringBootApplication
public class BackApplication implements CommandLineRunner {
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("Test123"));

		Function<String, String> helloWorldLambda = name -> {
			System.out.println("I'm inside the lambda function exp -> " + name);
			return name.toUpperCase();
		};

		System.out.println(helloWorldLambda.apply("Prueba1"));
	}
}
