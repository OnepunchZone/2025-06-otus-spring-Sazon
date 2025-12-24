package ru.otus.hw.mappers;

import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.models.Book;

public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);
}
