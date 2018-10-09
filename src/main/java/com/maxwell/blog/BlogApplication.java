package com.maxwell.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.maxwell.blog.service.DBFileStorageService;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Autowired
	private DBFileStorageService service;

	@Bean
	CommandLineRunner init() {
		return (args) -> {
			//String fileNameAfter = "Payslip 450 (23-08-2018).pdf".replaceAll("\\s", "_");
			//System.out.println(fileNameAfter);
		};
	}

}
