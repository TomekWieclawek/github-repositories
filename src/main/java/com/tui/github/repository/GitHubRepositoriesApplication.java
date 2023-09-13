package com.tui.github.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GitHubRepositoriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubRepositoriesApplication.class, args);
	}

}
