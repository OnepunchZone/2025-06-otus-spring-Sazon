package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.bookdtos.BookCreateDto;
import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.dtos.bookdtos.BookUpdateDto;
import ru.otus.hw.services.BookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public Flux<BookDto> listBooks() {
        return bookService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> createBook(@Valid @RequestBody BookCreateDto bookCreate) {
        return bookService.create(bookCreate);
    }

    @GetMapping("/{id}")
    public Mono<BookDto> getBook(@PathVariable String id) {
        return bookService.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<BookDto> editBook(@PathVariable String id, @Valid @RequestBody BookUpdateDto bookUpdateDto) {
        bookUpdateDto.setId(id);

        return bookService.update(bookUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable String id) {
        return bookService.deleteById(id);
    }
}
