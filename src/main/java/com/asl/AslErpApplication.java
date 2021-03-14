package com.asl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import com.asl.security.SpringSecurityAuditorAware;

@SpringBootApplication
@MapperScan("com.asl.mapper")
public class AslErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(AslErpApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}

}
