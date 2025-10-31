package ru.otus.hw.repositories;

public interface BookRepositoryCustom {
    void deleteBooksByAuthorId(String authorId);

    void deleteBooksByGenreId(String genreId);
}
