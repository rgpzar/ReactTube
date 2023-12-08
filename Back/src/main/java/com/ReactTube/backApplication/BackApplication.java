package com.ReactTube.backApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan(value = "com.ReactTube.backApplication.models")
public class BackApplication implements CommandLineRunner {
	private final InitialDataLoader dataLoader;
	private final PasswordEncoder passwordEncoder;

	BackApplication(
			@Autowired InitialDataLoader dataLoader,
			@Autowired PasswordEncoder passwordEncoder
	)
	{
		this.dataLoader = dataLoader;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		dataLoader.loadUserList();
		//dataLoader.loadVideoList();
		System.out.println(passwordEncoder.encode("Test123"));
	}
}
