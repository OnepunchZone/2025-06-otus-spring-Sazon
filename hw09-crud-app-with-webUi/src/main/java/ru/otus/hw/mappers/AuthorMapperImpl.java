package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapperImpl implements AuthorMapper {
    @Override
    public AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFullName(author.getFullName());

        return authorDto;
    }

    @Override
    public Author toEntity(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }

        Author author = new Author();
        author.setId(authorDto.getId());
        author.setFullName(authorDto.getFullName());

        return author;
    }
}
