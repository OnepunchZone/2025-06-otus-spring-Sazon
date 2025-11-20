package ru.otus.hw.mapper;

import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.model.Comment;

public interface CommentMapper {
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);
}
