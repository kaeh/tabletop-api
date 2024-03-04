package com.tabletop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.tabletop")
@EnableJpaRepositories(basePackages = "com.tabletop.persistence.repository")
@EntityScan(basePackages = "com.tabletop.persistence.model")
public class TabletopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TabletopApplication.class, args);
	}

}
