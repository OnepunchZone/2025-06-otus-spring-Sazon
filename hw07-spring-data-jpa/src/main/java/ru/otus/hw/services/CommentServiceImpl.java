package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Comment findById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment create(String text, long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(bookId))
                );

        Comment comment = new Comment(0, text, book);

        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(long id, String text) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment with id %d not found".formatted(id))
        );

        comment.setText(text);

        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
