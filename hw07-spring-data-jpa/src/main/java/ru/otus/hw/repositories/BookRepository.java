package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"author", "genre"})
    @Query("select b from Book b")
    List<Book> findAll();

    @EntityGraph(attributePaths = {"author", "genre", "comments"})
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findById(@Param("id") long id);

}
