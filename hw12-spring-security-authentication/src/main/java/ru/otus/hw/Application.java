package ru.otus.hw;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Application.class, args);
		Console.main(args);
		/*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("user: " + encoder.encode("user"));
		System.out.println("admin: " + encoder.encode("admin"));*/
	}
}
