package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.commentdtos.CommentCreateDto;
import ru.otus.hw.dtos.commentdtos.CommentDto;

public interface CommentService {
    Mono<CommentDto> findById(String id);

    Flux<CommentDto> findByBookId(String bookId);


    Mono<CommentDto> create(CommentCreateDto commentCreateDto);

    Mono<CommentDto> update(CommentDto commentDto);

    Mono<Void> deleteById(String id);
}
