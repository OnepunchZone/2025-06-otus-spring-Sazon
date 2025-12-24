package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;

@WebFluxTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    private final BookDto bookDto = new BookDto(
            "1",
            "Test Book",
            new AuthorDto("1", "Test Author"),
            new GenreDto("1", "Test Genre"),
            new ArrayList<>()
    );

    @Test
    void shouldReturnListOfBooks() {
        given(bookService.findAll()).willReturn(Flux.just(bookDto));

        webTestClient.get()
                .uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("Test Book");
    }

    @Test
    void shouldReturnBookById() {
        given(bookService.findById("1")).willReturn(Mono.just(bookDto));

        webTestClient.get()
                .uri("/api/v1/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Test Book");
    }
}