package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.commentdtos.CommentCreateDto;
import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    public Mono<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id %s not found".formatted(id))))
                .map(commentMapper::toDto);
    }

    @Override
    public Flux<CommentDto> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
                .map(commentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> create(CommentCreateDto commentCreateDto) {
        return bookRepository.findById(commentCreateDto.getBookId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found")))
                .flatMap(book -> {
                    Comment comment = new Comment();
                    comment.setText(commentCreateDto.getText());
                    comment.setBookId(commentCreateDto.getBookId());

                    return commentRepository.save(comment)
                            .flatMap(savedComment -> {

                                if (book.getComments() == null) {
                                    book.setComments(new ArrayList<>());
                                }
                                book.getComments().add(savedComment);

                                return bookRepository.save(book)
                                        .thenReturn(savedComment);
                            });
                })
                .map(commentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> update(CommentDto commentDto) {
        return commentRepository.findById(commentDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment not found")))
                .flatMap(comment -> {
                    comment.setText(commentDto.getText());

                    return commentRepository.save(comment);
                })
                .map(commentMapper::toDto);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.findById(id)
                .flatMap(comment -> {
                    String bookId = comment.getBookId();

                    if (bookId == null) {
                        return commentRepository.deleteById(id);
                    }

                    return bookRepository.findById(bookId)
                            .flatMap(book -> {
                                if (book.getComments() != null) {
                                    book.getComments().removeIf(c -> c.getId().equals(id));

                                    return bookRepository.save(book)
                                            .then(commentRepository.deleteById(id));
                                }
                                return commentRepository.deleteById(id);
                            })
                            .switchIfEmpty(commentRepository.deleteById(id));
                });
    }
}
