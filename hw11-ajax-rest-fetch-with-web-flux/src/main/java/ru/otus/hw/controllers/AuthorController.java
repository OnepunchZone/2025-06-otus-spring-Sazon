package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.services.AuthorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public Flux<AuthorDto> listAuthors() {
        return authorService.findAll();
    }
}
