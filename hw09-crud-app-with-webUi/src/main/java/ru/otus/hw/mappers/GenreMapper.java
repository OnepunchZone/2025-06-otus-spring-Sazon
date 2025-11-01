package ru.otus.hw.mappers;

import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.models.Genre;

public interface GenreMapper {
    GenreDto toDto(Genre genre);

    Genre toEntity(GenreDto genreDto);
}
