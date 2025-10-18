package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner dataChecker(AuthorRepository authorRepository,
										 BookRepository bookRepository,
										 GenreRepository genreRepository) {
		return args -> {

			System.out.println("Авторы:");
			authorRepository.findAll().forEach(author ->
					System.out.println("  " + author.getId() + ": " + author.getFullName())
			);

			System.out.println("Жанры:");
			genreRepository.findAll().forEach(genre ->
					System.out.println("  " + genre.getId() + ": " + genre.getName())
			);

			System.out.println("Книги:");
			bookRepository.findAll().forEach(book ->
					System.out.println("  " + book.getId() + ": " + book.getTitle() +
							" (автор: " + book.getAuthor().getFullName() +
							", жанр: " + book.getGenre().getName() + ")")
			);
		};
	}

}
