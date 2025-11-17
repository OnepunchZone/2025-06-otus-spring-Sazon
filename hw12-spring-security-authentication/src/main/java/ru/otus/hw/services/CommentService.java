package ru.otus.hw.services;

import ru.otus.hw.dtos.commentdtos.CommentCreateDto;
import ru.otus.hw.dtos.commentdtos.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findByBookId(long bookId);

    CommentDto create(CommentCreateDto commentCreateDto);

    CommentDto update(CommentDto commentDto);

    void deleteById(long id);
}
