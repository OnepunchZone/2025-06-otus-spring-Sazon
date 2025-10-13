package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcOperations jdbc;

    public JdbcBookRepository(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        List<Book> books = jdbc.query(
                "select b.id as book_id, b.title as book_title, " +
                        "a.id as author_id, a.full_name as author_name, " +
                        "g.id as genre_id, g.name as genre_name " +
                        "from books b " +
                        "left join authors a on b.author_id = a.id " +
                        "left join genres g on b.genre_id = g.id " +
                        "where b.id = :id",
                params,
                new BookRowMapper()
        );

        return books.stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("select b.id as book_id, b.title as book_title, " +
                "a.id as author_id, a.full_name as author_name, " +
                "g.id as genre_id, g.name as genre_name " +
                "from books b left join authors a on b.author_id = a.id left join genres g on b.genre_id = g.id ",
                new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }

        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("delete from books where id = :id", params);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        jdbc.update(
                "insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                params,
                keyHolder,
                new String[]{"id"}
        );

        book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return book;
    }

    private Book update(Book book) {
        Map<String, Object> params = Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId()
        );

        int updatedRows = jdbc.update(
                "update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id",
                params
        );

        if (updatedRows == 0) {
            throw new EntityNotFoundException("Book with id = " + book.getId() + " not found");
        }

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong("author_id"));
            author.setFullName(rs.getString("author_name"));

            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));

            return new Book(rs.getLong("book_id"), rs.getString("book_title"), author, genre);
        }
    }
}
