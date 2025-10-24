package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
class BookRepositoryImplTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Optional<Book> currentBook = bookRepository.findById(1L);

        assertThat(currentBook).isPresent();
        Book book = currentBook.get();

        Book expectedBook = em.find(Book.class, 1L);
        assertThat(book).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> actualBooks = bookRepository.findAll();

        assertThat(actualBooks).hasSize(3);

        Book expectedBook1 = em.find(Book.class, 1L);
        Book expectedBook2 = em.find(Book.class, 2L);
        Book expectedBook3 = em.find(Book.class, 3L);

        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedBook1, expectedBook2, expectedBook3);

        actualBooks.forEach(book -> {
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getGenre()).isNotNull();
        });
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Author author = em.find(Author.class, 1L);
        Genre genre = em.find(Genre.class, 1L);

        Book newBook = new Book();
        newBook.setTitle("BookTitle_22");
        newBook.setAuthor(author);
        newBook.setGenre(genre);

        Book savedBook = bookRepository.save(newBook);

        Book expectedBook = new Book();
        expectedBook.setId(savedBook.getId());
        expectedBook.setTitle("BookTitle_22");
        expectedBook.setAuthor(author);
        expectedBook.setGenre(genre);

        assertThat(savedBook).usingRecursiveComparison().isEqualTo(expectedBook);

        Book foundBook = em.find(Book.class, savedBook.getId());
        assertThat(foundBook.getTitle()).isEqualTo("BookTitle_22");
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book existingBook = em.find(Book.class, 1L);
        Author newAuthor = em.find(Author.class, 2L);
        Genre newGenre = em.find(Genre.class, 2L);

        existingBook.setTitle("Updated_Book_Title");
        existingBook.setAuthor(newAuthor);
        existingBook.setGenre(newGenre);

        Book updatedBook = bookRepository.save(existingBook);

        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setTitle("Updated_Book_Title");
        expectedBook.setAuthor(newAuthor);
        expectedBook.setGenre(newGenre);

        assertThat(updatedBook).usingRecursiveComparison()
                .ignoringFields("comments").isEqualTo(expectedBook);

        Book foundBook = em.find(Book.class, 1L);
        assertThat(foundBook).usingRecursiveComparison()
                .ignoringFields("comments").isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepository.findById(1L)).isPresent();

        bookRepository.deleteById(1L);

        assertThat(bookRepository.findById(1L)).isEmpty();

        Book deletedBook = em.find(Book.class, 1L);
        assertThat(deletedBook).isNull();
    }

    @DisplayName("должен загружать книгу с комментариями")
    @Test
    void shouldLoadBookWithComments() {
        Book book = bookRepository.findById(1L).orElseThrow();

        assertThat(book).isNotNull();
        assertThat(book.getComments()).isNotNull();
    }

}