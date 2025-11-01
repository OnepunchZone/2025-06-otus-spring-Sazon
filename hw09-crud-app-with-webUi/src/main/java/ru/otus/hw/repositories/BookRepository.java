package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "book-author-genre-graph")
    @Override
    List<Book> findAll();

    @EntityGraph(value = "book-author-genre-graph")
    @Override
    Optional<Book> findById(Long id);

    @EntityGraph(value = "book-with-comments-graph")
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdWithComments(@Param("id") Long id);

}
