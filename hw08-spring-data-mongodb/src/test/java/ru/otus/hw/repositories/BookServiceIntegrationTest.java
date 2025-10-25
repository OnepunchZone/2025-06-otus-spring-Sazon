package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class})
@Testcontainers
@DisplayName("Сервис книг с MongoDB")
class BookServiceIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        initTestData();
    }

    private void initTestData() {
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("comments");

        mongoTemplate.createCollection("authors");
        mongoTemplate.createCollection("genres");
        mongoTemplate.createCollection("books");
        mongoTemplate.createCollection("comments");

        Author author1 = new Author("507f1f77bcf86cd799439011", "Author_1");
        Author author2 = new Author("507f1f77bcf86cd799439012", "Author_2");
        Author author3 = new Author("507f1f77bcf86cd799439013", "Author_3");
        authorRepository.saveAll(List.of(author1, author2, author3));

        Genre genre1 = new Genre("607f1f77bcf86cd799439021", "Genre_1");
        Genre genre2 = new Genre("607f1f77bcf86cd799439022", "Genre_2");
        Genre genre3 = new Genre("607f1f77bcf86cd799439023", "Genre_3");
        genreRepository.saveAll(List.of(genre1, genre2, genre3));

        Book book1 = new Book("707f1f77bcf86cd799439031", "BookTitle_1", author1, genre1);
        Book book2 = new Book("707f1f77bcf86cd799439032", "BookTitle_2", author2, genre2);
        Book book3 = new Book("707f1f77bcf86cd799439033", "BookTitle_3", author3, genre3);
        bookRepository.saveAll(List.of(book1, book2, book3));

        Comment comment1 = new Comment("807f1f77bcf86cd799439041", "Comment_1 for Book_1", book1);
        Comment comment2 = new Comment("807f1f77bcf86cd799439042", "Comment_2 for Book_1", book1);
        Comment comment3 = new Comment("807f1f77bcf86cd799439043", "Comment_1 for Book_2", book2);
        commentRepository.saveAll(List.of(comment1, comment2, comment3));
    }


    @Test
    @DisplayName("должен находить книгу по id")
    void shouldFindBookById() {
        Optional<Book> book = bookService.findById("707f1f77bcf86cd799439031");

        assertThat(book).isPresent();

        assertThat(book.get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Book("707f1f77bcf86cd799439031", "BookTitle_1",
                        new Author("507f1f77bcf86cd799439011", "Author_1"),
                        new Genre("607f1f77bcf86cd799439021", "Genre_1")));
    }

    @Test
    @DisplayName("должен находить все книги")
    void shouldFindAllBooks() {
        List<Book> books = bookService.findAll();

        assertThat(books).hasSize(3);

        assertThat(books)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrder(
                        new Book("707f1f77bcf86cd799439031", "BookTitle_1",
                                new Author("507f1f77bcf86cd799439011", "Author_1"),
                                new Genre("607f1f77bcf86cd799439021", "Genre_1")),
                        new Book("707f1f77bcf86cd799439032", "BookTitle_2",
                                new Author("507f1f77bcf86cd799439012", "Author_2"),
                                new Genre("607f1f77bcf86cd799439022", "Genre_2")),
                        new Book("707f1f77bcf86cd799439033", "BookTitle_3",
                                new Author("507f1f77bcf86cd799439013", "Author_3"),
                                new Genre("607f1f77bcf86cd799439023", "Genre_3"))
                );
    }

    @Test
    @DisplayName("должен сохранять новую книгу")
    void shouldSaveNewBook() {
        Author author = authorRepository.findAll().get(0);
        Genre genre = genreRepository.findAll().get(0);

        Book savedBook = bookService.insert("New Book", author.getId(), genre.getId());

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");

        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        assertThat(foundBook).isPresent();
    }

    @Test
    @DisplayName("должен удалять книгу по id с комментами")
    void shouldDeleteBookById() {
        String bookId = "707f1f77bcf86cd799439031";

        assertThat(bookRepository.findById(bookId)).isPresent();

        List<Comment> commentsBeforeDelete = commentRepository.findByBookId(bookId);
        assertThat(commentsBeforeDelete).hasSize(2);
        assertThat(commentsBeforeDelete)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("Comment_1 for Book_1", "Comment_2 for Book_1");

        bookService.deleteById(bookId);

        assertThat(bookRepository.findById(bookId)).isEmpty();

        List<Comment> commentsAfterDelete = commentRepository.findByBookId(bookId);
        assertThat(commentsAfterDelete).isEmpty();
    }
}
