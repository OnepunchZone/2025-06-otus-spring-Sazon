package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToString(Book book) {

        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre())
        );
    }

    public String bookToStringWithComments (Book book) {

        return "Id: %d, title: %s, author: {%s}, genres: [%s], comments: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre()),
                book.getComments().stream()
                        .map(comment -> commentConverter.commentToString(comment))
                        .collect(Collectors.joining(", "))
        );
    }
}
