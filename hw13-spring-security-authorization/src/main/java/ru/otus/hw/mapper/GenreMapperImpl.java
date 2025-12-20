package ru.otus.hw.mapper;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

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
