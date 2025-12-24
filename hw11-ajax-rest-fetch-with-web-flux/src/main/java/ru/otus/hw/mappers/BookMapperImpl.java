package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookMapperImpl implements BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    private final CommentMapper commentMapper;

    @Override
    public BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(authorMapper.toDto(book.getAuthor()));
        bookDto.setGenre(genreMapper.toDto(book.getGenre()));

        if (book.getComments() != null && !book.getComments().isEmpty()) {
            List<CommentDto> commentDtos = book.getComments().stream()
                    .map(commentMapper::toDto)
                    .collect(Collectors.toList());
            bookDto.setComments(commentDtos);
        } else {
            bookDto.setComments(new ArrayList<>());
        }

        return bookDto;
    }

    @Override
    public Book toEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(authorMapper.toEntity(bookDto.getAuthor()));
        book.setGenre(genreMapper.toEntity(bookDto.getGenre()));

        if (bookDto.getComments() != null && !bookDto.getComments().isEmpty()) {
            List<Comment> comments = bookDto.getComments().stream()
                    .map(commentMapper::toEntity)
                    .collect(Collectors.toList());
            book.setComments(comments);
        }

        return book;
    }
}
