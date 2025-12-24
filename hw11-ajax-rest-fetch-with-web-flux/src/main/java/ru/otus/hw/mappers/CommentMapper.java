package ru.otus.hw.mappers;

import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentMapper {
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);

    List<CommentDto> toDtoList(List<Comment> comments);
}
