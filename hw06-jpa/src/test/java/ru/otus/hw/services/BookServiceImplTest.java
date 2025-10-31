package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис книг без транзакций ")
@DataJpaTest
@Import({BookServiceImpl.class, JpaBookRepository.class,
        JpaAuthorRepository.class, JpaGenreRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен работать findById с явной загрузкой комментариев")
    @Test
    void shouldWorkWithEagerLoading() {
        Book book = bookService.findById(1L).orElseThrow();

        assertThat(book).isNotNull();
        assertThat(book.getComments()).isNotNull();
    }

    @DisplayName("должен работать findAll без транзакции")
    @Test
    void shouldFindAllWithoutTransaction() {
        var books = bookService.findAll();

        assertThat(books).isNotNull().hasSize(3);

        books.forEach(book -> {
            assertThat(book.getId()).isPositive();
            assertThat(book.getTitle()).isNotBlank();
        });
    }
}
