package ru.otus.hw.services;

import ru.otus.hw.dtos.bookdtos.BookCreateDto;
import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.dtos.bookdtos.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookCreateDto bookCreateDto);

    BookDto update(BookUpdateDto bookUpdateDto);

    void deleteById(long id);
}
