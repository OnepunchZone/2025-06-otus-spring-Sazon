package ru.otus.hw.service;

import ru.otus.hw.dto.comment.CommentCreateDto;
import ru.otus.hw.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findByBookId(long bookId);

    CommentDto create(CommentCreateDto commentCreateDto);

    CommentDto update(CommentDto commentDto);

    void deleteById(long id);
}
