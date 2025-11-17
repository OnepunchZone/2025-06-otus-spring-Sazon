package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setBookId(comment.getBook() != null ? comment.getBook().getId() : null);

        return commentDto;
    }

    @Override
    public Comment toEntity(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());

        if (commentDto.getBookId() != null) {
            Book book = new Book();
            book.setId(commentDto.getBookId());
            comment.setBook(book);
        }

        return comment;
    }
}
