package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapperImpl implements GenreMapper {
    @Override
    public GenreDto toDto(Genre genre) {

        if (genre == null) {
            return null;
        }

        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());

        return genreDto;
    }

    @Override
    public Genre toEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }

        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());

        return genre;
    }
}
