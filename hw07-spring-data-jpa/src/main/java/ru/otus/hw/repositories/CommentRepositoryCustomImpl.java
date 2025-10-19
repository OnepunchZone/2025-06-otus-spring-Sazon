package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final BookRepository bookRepository;

    @Override
    public List<Comment> findByBookId(long bookId) {
        return bookRepository.findById(bookId).map(Book::getComments).orElse(Collections.emptyList());
    }
}
