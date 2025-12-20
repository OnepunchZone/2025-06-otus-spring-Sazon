package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDto>> listGenres() {
        List<GenreDto> genresLst = genreService.findAll();

        return ResponseEntity.ok(genresLst);
    }
}
