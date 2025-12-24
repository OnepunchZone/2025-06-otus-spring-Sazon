package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.commentdtos.CommentCreateDto;
import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.services.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CommentDto> createComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        return commentService.create(commentCreateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteComment(@PathVariable String id) {
        return commentService.deleteById(id);
    }

    @GetMapping("/book/{bookId}")
    public Flux<CommentDto> getCommentsByBook(@PathVariable String bookId) {
        return commentService.findByBookId(bookId);
    }
}
