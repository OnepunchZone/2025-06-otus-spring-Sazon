package ru.otus.hw.mappers;

import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.models.Comment;

public interface CommentMapper {
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);
}
