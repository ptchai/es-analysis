package com.es.analysis;

import com.es.analysis.services.NumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalysisApplication implements CommandLineRunner {

	private final NumberService numberService;

	@Autowired
	public AnalysisApplication(NumberService numberService) {
		this.numberService = numberService;
	}

	public static void main(String[] args) {
		SpringApplication.run(AnalysisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		numberService.perform();
	} 
}
