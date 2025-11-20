package ru.otus.hw.mapper;

import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.model.Book;

public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);
}
