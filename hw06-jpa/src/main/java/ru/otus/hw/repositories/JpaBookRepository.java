package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-graph");
        return Optional.ofNullable(
                em.find(Book.class, id, java.util.Map.of("jakarta.persistence.fetchgraph", entityGraph))
        );
    }

    @Override
    public Optional<Book> finByIdWithComments(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-with-comments-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.id = :id", Book.class);

        query.setParameter("id", id);
        query.setHint("jakarta.persistence.fetchgraph", entityGraph);

        List<Book> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-graph");
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b", Book.class);

        query.setHint("jakarta.persistence.fetchgraph", entityGraph);

        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }

        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Book book = em.getReference(Book.class, id);

        em.remove(book);
    }
}
