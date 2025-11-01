package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.commentdtos.CommentCreateDto;
import ru.otus.hw.dtos.commentdtos.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));

        return commentMapper.toDto(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByBookId(long bookId) {

        return commentRepository.findByBookId(bookId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto create(CommentCreateDto commentCreateDto) {
        var book = bookRepository.findById(commentCreateDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(commentCreateDto.getBookId()))
                );

        Comment comment = new Comment();
        comment.setText(commentCreateDto.getText());
        comment.setBook(book);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Comment with id %d not found".formatted(commentDto.getId()))
        );

        comment.setText(commentDto.getText());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
