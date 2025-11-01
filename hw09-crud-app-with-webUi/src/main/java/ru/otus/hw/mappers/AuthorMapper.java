package ru.otus.hw.mappers;

import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.models.Author;

public interface AuthorMapper {
    AuthorDto toDto(Author author);

    Author toEntity(AuthorDto authorDto);
}
