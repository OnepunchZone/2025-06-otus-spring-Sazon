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
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.dtos.bookdtos.BookCreateDto;
import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.dtos.bookdtos.BookUpdateDto;
import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.CommentMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис книг без транзакций")
@DataJpaTest
@Import({BookServiceImpl.class,
        AuthorMapperImpl.class,
        GenreMapperImpl.class,
        CommentMapperImpl.class,
        BookMapperImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional(propagation = Propagation.NEVER)
public class BookServiceImplTest {
    @Autowired
    private BookService bookService;

    @DisplayName("должен работать findById с явной загрузкой комментариев")
    @Test
    @Order(1)
    void shouldWorkWithEagerLoading() {
        AuthorDto expectedAuthorDto = new AuthorDto(1L, "Author_1");
        GenreDto expectedGenreDto = new GenreDto(1L, "Genre_1");
        CommentDto expectedCommentDto = new CommentDto(1L, "Comment_1", 1L);
        List<CommentDto> expectedComments = List.of(expectedCommentDto);

        BookDto expectedBookDto = new BookDto(1L, "BookTitle_1", expectedAuthorDto,
                expectedGenreDto, expectedComments);

        BookDto actualBook = bookService.findById(1L);

        assertThat(actualBook).isNotNull();
        assertThat(actualBook).usingRecursiveComparison()
                .isEqualTo(expectedBookDto);

        assertThat(actualBook.getComments())
                .isNotEmpty()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedCommentDto);
    }

    @DisplayName("должен работать findAll без транзакции")
    @Test
    @Order(2)
    void shouldFindAllWithoutTransaction() {
        List<BookDto> books = bookService.findAll();

        assertThat(books)
                .isNotNull()
                .hasSize(3)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("comments")
                .containsExactlyInAnyOrder(
                        new BookDto(1L, "BookTitle_1", new AuthorDto(1L, "Author_1"),
                                new GenreDto(1L, "Genre_1"), null),
                        new BookDto(2L, "BookTitle_2", new AuthorDto(2L, "Author_2"),
                                new GenreDto(2L, "Genre_2"), null),
                        new BookDto(3L, "BookTitle_3", new AuthorDto(3L, "Author_3"),
                                new GenreDto(3L, "Genre_3"), null)
                );
    }

    @DisplayName("должен создавать новую книгу")
    @Test
    void shouldInsertBook() {
        String title = "New Book";
        long authorId = 1L;
        long genreId = 1L;

        BookCreateDto createDto = new BookCreateDto(title, authorId, genreId);
        BookDto savedBook = bookService.create(createDto);

        assertThat(savedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("comments")
                .isEqualTo(new BookDto(4L, title, new AuthorDto(authorId, "Author_1"),
                        new GenreDto(genreId, "Genre_1"), null));
    }

    @DisplayName("должен обновлять существующую книгу")
    @Test
    void shouldUpdateBook() {
        long bookId = 1L;
        String newTitle = "Updated Book Title";
        long newAuthorId = 2L;
        long newGenreId = 2L;

        BookDto bookBeforeUpdate = bookService.findById(bookId);
        List<CommentDto> originalComments = bookBeforeUpdate.getComments();

        BookUpdateDto updateDto = new BookUpdateDto(bookId, newTitle, newAuthorId, newGenreId);
        BookDto updatedBook = bookService.update(updateDto);

        BookDto expectedBook = new BookDto(
                bookId,
                newTitle,
                new AuthorDto(newAuthorId, "Author_2"),
                new GenreDto(newGenreId, "Genre_2"),
                originalComments
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

        BookDto bookBeforeDelete = bookService.findById(bookId);
        assertThat(bookBeforeDelete).isNotNull();

        bookService.deleteById(bookId);

        assertThatThrownBy(() -> bookService.findById(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Book with id " + bookId + " not found");
    }

    @DisplayName("должен возвращать null для несуществующей книги")
    @Test
    void shouldReturnEmptyOptionalForNonExistingBook() {
        long bookId = 9L;

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );

        assertThat(exception.getMessage())
                .contains("Book with id " + bookId + " not found");
    }

    @DisplayName("должен загружать книгу с комментариями")
    @Test
    @Order(3)
    void shouldFindBookWithCommentsUsingEntityGraph() {
        BookDto bookWithComments = bookService.findById(1L);

        assertThat(bookWithComments).isNotNull();
        assertThat(bookWithComments.getComments())
                .isNotNull();

        CommentDto expectedComment = new CommentDto(1L, "Comment_1", 1L);

        assertThat(bookWithComments.getComments().get(0)).usingRecursiveComparison().isEqualTo(expectedComment);
    }
}
