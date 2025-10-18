package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class})
class JpaBookRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private JpaBookRepository repositoryJpa;

    @Autowired
    private JpaAuthorRepository authorRepository;

    @Autowired
    private JpaGenreRepository genreRepository;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Optional<Book> currentBook = repositoryJpa.findById(1L);

        assertThat(currentBook).isPresent();
        Book book = currentBook.get();
        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("BookTitle_1");
        assertThat(book.getAuthor().getFullName()).isEqualTo("Author_1");
        assertThat(book.getGenre().getName()).isEqualTo("Genre_1");
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> actualBooks = repositoryJpa.findAll();

        assertThat(actualBooks).hasSize(3);

        Book firstBook = actualBooks.get(0);
        assertThat(firstBook.getId()).isEqualTo(1L);
        assertThat(firstBook.getTitle()).isEqualTo("BookTitle_1");
        assertThat(firstBook.getAuthor().getFullName()).isEqualTo("Author_1");
        assertThat(firstBook.getGenre().getName()).isEqualTo("Genre_1");

        actualBooks.forEach(book -> {
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getGenre()).isNotNull();
        });
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Author author = authorRepository.findById(1L).orElseThrow();
        Genre genre = genreRepository.findById(1L).orElseThrow();

        Book newBook = new Book();
        newBook.setTitle("BookTitle_22");
        newBook.setAuthor(author);
        newBook.setGenre(genre);

        Book savedBook = repositoryJpa.save(newBook);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isGreaterThan(0);
        assertThat(savedBook.getTitle()).isEqualTo("BookTitle_22");
        assertThat(savedBook.getAuthor().getId()).isEqualTo(1L);
        assertThat(savedBook.getGenre().getId()).isEqualTo(1L);

        Book foundBook = em.find(Book.class, savedBook.getId());
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("BookTitle_22");
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book existingBook = repositoryJpa.findById(1L).orElseThrow();
        Author newAuthor = authorRepository.findById(2L).orElseThrow();
        Genre newGenre = genreRepository.findById(2L).orElseThrow();

        existingBook.setTitle("Updated_Book_Title");
        existingBook.setAuthor(newAuthor);
        existingBook.setGenre(newGenre);

        Book updatedBook = repositoryJpa.save(existingBook);

        assertThat(updatedBook.getId()).isEqualTo(1L);
        assertThat(updatedBook.getTitle()).isEqualTo("Updated_Book_Title");
        assertThat(updatedBook.getAuthor().getId()).isEqualTo(2L);
        assertThat(updatedBook.getGenre().getId()).isEqualTo(2L);

        Book foundBook = em.find(Book.class, 1L);
        assertThat(foundBook.getTitle()).isEqualTo("Updated_Book_Title");
        assertThat(foundBook.getAuthor().getId()).isEqualTo(2L);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(repositoryJpa.findById(1L)).isPresent();

        repositoryJpa.deleteById(1L);

        assertThat(repositoryJpa.findById(1L)).isEmpty();

        Book deletedBook = em.find(Book.class, 1L);
        assertThat(deletedBook).isNull();
    }

    @DisplayName("должен загружать книгу с комментариями")
    @Test
    void shouldLoadBookWithComments() {
        Book book = repositoryJpa.findById(1L).orElseThrow();

        assertThat(book).isNotNull();
        assertThat(book.getComments()).isNotNull();
    }

}