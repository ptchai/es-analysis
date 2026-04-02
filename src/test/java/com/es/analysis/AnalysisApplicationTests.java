package com.es.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AnalysisApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodRunsSuccessfully() {
		String[] args = {"--test.property=abc"};
		ConfigurableApplicationContext context = null;
		try {
			AnalysisApplication.main(args);
			context = SpringApplication.run(AnalysisApplication.class, args);
			assertNotNull(context);
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}
}
