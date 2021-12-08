package com.internship.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class InternshipApplication implements CommandLineRunner {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(InternshipApplication.class, args);
	}

	@Override	public void run(String... args) throws Exception {
//		String sql = "CREATE TABLE Persons (\n" +
//				"    PersonID int,\n" +
//				"    LastName varchar(255),\n" +
//				"    FirstName varchar(255),\n" +
//				"    Address varchar(255),\n" +
//				"    City varchar(255)\n" +
//				");";
//		jdbcTemplate.execute(sql);
	}
}
