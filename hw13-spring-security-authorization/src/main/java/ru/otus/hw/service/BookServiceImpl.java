package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookUpdateDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        Book book = bookRepository.findByIdWithComments(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));

        return bookMapper.toDto(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BookDto create(BookCreateDto bookCreateDto) {
        var author = authorRepository.findById(bookCreateDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                        .formatted(bookCreateDto.getAuthorId())));
        var genre = genreRepository.findById(bookCreateDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found"
                        .formatted(bookCreateDto.getGenreId())));
        var book = new Book(0, bookCreateDto.getTitle(), author, genre, null);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(BookUpdateDto bookUpdateDto) {
        var updatingBook = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found"
                        .formatted(bookUpdateDto.getId())));
        var author = authorRepository.findById(bookUpdateDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                        .formatted(bookUpdateDto.getAuthorId())));
        var genre = genreRepository.findById(bookUpdateDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found"
                        .formatted(bookUpdateDto.getGenreId())));

        updatingBook.setTitle(bookUpdateDto.getTitle());
        updatingBook.setAuthor(author);
        updatingBook.setGenre(genre);

        return bookMapper.toDto(bookRepository.save(updatingBook));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
