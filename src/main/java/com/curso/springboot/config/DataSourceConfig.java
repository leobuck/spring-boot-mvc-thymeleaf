package com.curso.springboot.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource getDataSource() {
		return DataSourceBuilder.create()
				.driverClassName("org.postgresql.Driver")
				.url("jdbc:postgresql://ec2-3-89-230-115.compute-1.amazonaws.com:5432/d5eicj8v147let?sslmode=require")
				.username("gntagxlrlmwvzv")
				.password("b0cb3c1a97f22eed86390e5155656ee6795bd7859333448094287aabe9b2a8ce")
				.build();
	}
}
