package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//тут уже никак без игнора не проходят.
//либо я не соображу как чз usingRecursiveComparison сделать это
@DisplayName("Сервис книг без транзакций ")
@DataJpaTest
@Import(BookServiceImpl.class)
@Transactional(propagation = Propagation.NEVER)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("должен работать findById с явной загрузкой комментариев")
    @Test
    @Order(1)
    void shouldWorkWithEagerLoading() {
        Author expectedAuthor = new Author(1L, "Author_1");
        Genre expectedGenre = new Genre(1L, "Genre_1");

        Comment expectedComment = new Comment(1L, "Comment_1", null);
        List<Comment> expectedComments = List.of(expectedComment);

        Book expectedBook = new Book(1L, "BookTitle_1", expectedAuthor, expectedGenre, expectedComments);

        Optional<Book> actualBook = bookService.findById(1L);

        assertThat(actualBook).isPresent();

        assertThat(actualBook.get()).usingRecursiveComparison()
                .ignoringFields("comments.book").isEqualTo(expectedBook);

        assertThat(actualBook.get().getComments())
                .isNotEmpty()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("book")
                .containsExactly(expectedComment);
    }

    @DisplayName("должен работать findAll без транзакции")
    @Test
    @Order(2)
    void shouldFindAllWithoutTransaction() {
        var books = bookService.findAll();

        assertThat(books)
                .isNotNull()
                .hasSize(3)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("comments")
                .containsExactlyInAnyOrder(
                        new Book(1L, "BookTitle_1", new Author(1L, "Author_1"),
                                new Genre(1L, "Genre_1"), null),
                        new Book(2L, "BookTitle_2", new Author(2L, "Author_2"),
                                new Genre(2L, "Genre_2"), null),
                        new Book(3L, "BookTitle_3", new Author(3L, "Author_3"),
                                new Genre(3L, "Genre_3"), null)
                );
    }

    @DisplayName("должен создавать новую книгу")
    @Test
    void shouldInsertBook() {
        String title = "New Book";
        long authorId = 1L;
        long genreId = 1L;

        Book savedBook = bookService.insert(title, authorId, genreId);

        assertThat(savedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new Book(4L, title, new Author(authorId, "Author_1"),
                        new Genre(genreId, "Genre_1"), null));
    }

    @DisplayName("должен обновлять существующую книгу")
    @Test
    void shouldUpdateBook() {
        long bookId = 1L;
        String newTitle = "Updated Book Title";
        long newAuthorId = 2L;
        long newGenreId = 2L;

        Optional<Book> bookBeforeUpdate = bookService.findById(bookId);
        List<Comment> originalComments = bookBeforeUpdate.get().getComments();

        Book updatedBook = bookService.update(bookId, newTitle, newAuthorId, newGenreId);

        Book expectedBook = new Book(
                bookId,
                newTitle,
                new Author(newAuthorId, "Author_2"),
                new Genre(newGenreId, "Genre_2"),
                originalComments // Сохраняем оригинальные комментарии
        );

        assertThat(updatedBook)
                .usingRecursiveComparison()
                .ignoringFields("comments")
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBookById() {
        long bookId = 1L;

        bookService.deleteById(bookId);

        assertThat(bookService.findById(bookId)).isEmpty();
    }

    @DisplayName("должен возвращать пустой Optional для несуществующей книги")
    @Test
    void shouldReturnEmptyOptionalForNonExistingBook() {
        Optional<Book> result = bookService.findById(9L);

        assertThat(result).isEmpty();
    }

    @DisplayName("должен загружать книгу с комментариями через findByIdWithComments")
    @Test
    @Order(3)
    void shouldFindBookWithCommentsUsingEntityGraph() {
        Optional<Book> bookWithComments = bookService.findById(1L);

        assertThat(bookWithComments).isPresent();
        assertThat(bookWithComments.get().getComments())
                .hasSize(1)
                .extracting(Comment::getText)
                .containsExactly("Comment_1");
    }
}
