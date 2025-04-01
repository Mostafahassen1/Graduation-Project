package com.codemeet;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CodeMeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeMeetApplication.class, args);
	}

}
