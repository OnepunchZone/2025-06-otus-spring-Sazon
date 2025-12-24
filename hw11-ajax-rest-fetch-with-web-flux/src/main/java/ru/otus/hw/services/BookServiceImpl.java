package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.bookdtos.BookCreateDto;
import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.dtos.bookdtos.BookUpdateDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookMapper bookMapper;

    private final CommentMapper commentMapper;

    @Override
    public Mono<BookDto> findById(String id) {

        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id))))
                .flatMap(book -> Mono.zip(
                        Mono.just(book),
                        authorRepository.findById(book.getAuthor().getId()),
                        genreRepository.findById(book.getGenre().getId()),
                        commentRepository.findByBookId(id).collectList()
                ))
                .map(tuple -> {
                    Book book = tuple.getT1();
                    book.setAuthor(tuple.getT2());
                    book.setGenre(tuple.getT3());

                    List<Comment> comments = tuple.getT4();

                    BookDto bookDto = bookMapper.toDto(book);
                    bookDto.setComments(commentMapper.toDtoList(comments));

                    return bookDto;
                });
    }

    @Override
    public Flux<BookDto> findAll() {

        return bookRepository.findAll()
                .flatMap(book -> Mono.zip(
                        Mono.just(book),
                        authorRepository.findById(book.getAuthor().getId()),
                        genreRepository.findById(book.getGenre().getId())
                ))
                .map(tuple -> {
                    Book book = tuple.getT1();
                    book.setAuthor(tuple.getT2());
                    book.setGenre(tuple.getT3());

                    return bookMapper.toDto(book);
                });
    }

    @Override
    public Mono<BookDto> create(BookCreateDto bookCreateDto) {

        return Mono.zip(
                authorRepository.findById(bookCreateDto.getAuthorId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Author not found"))),
                genreRepository.findById(bookCreateDto.getGenreId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre not found")))
        ).flatMap(tuple -> {
            Book book = new Book();
            book.setTitle(bookCreateDto.getTitle());
            book.setAuthor(tuple.getT1());
            book.setGenre(tuple.getT2());

            return bookRepository.save(book);
        }).map(bookMapper::toDto);
    }

    @Override
    public Mono<BookDto> update(BookUpdateDto bookUpdateDto) {

        return bookRepository.findById(bookUpdateDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found")))
                .flatMap(book -> Mono.zip(
                        authorRepository.findById(bookUpdateDto.getAuthorId())
                                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author not found"))),
                        genreRepository.findById(bookUpdateDto.getGenreId())
                                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre not found")))
                ).map(tuple -> {
                    book.setTitle(bookUpdateDto.getTitle());
                    book.setAuthor(tuple.getT1());
                    book.setGenre(tuple.getT2());

                    return book;
                }))
                .flatMap(bookRepository::save)
                .map(bookMapper::toDto);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }
}
