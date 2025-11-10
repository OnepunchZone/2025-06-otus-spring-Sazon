package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteBooksByAuthorId(String authorId) {
        Query query = new Query(Criteria.where("author.$id").is(authorId));
        mongoTemplate.remove(query, Book.class);
    }

    @Override
    public void deleteBooksByGenreId(String genreId) {
        Query query = new Query(Criteria.where("genre.$id").is(genreId));
        mongoTemplate.remove(query, Book.class);
    }
}
