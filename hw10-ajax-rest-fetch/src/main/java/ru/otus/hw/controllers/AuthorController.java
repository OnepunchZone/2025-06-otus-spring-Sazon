package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDto>> listAuthors() {
        List<AuthorDto> authorList = authorService.findAll();

        return ResponseEntity.ok(authorList);
    }
}
