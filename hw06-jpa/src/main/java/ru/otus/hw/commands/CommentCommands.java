package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        try {
            var comment = commentService.findById(id);
            return commentConverter.commentToString(comment);
        } catch (Exception e) {
            return "Comment with id %d not found".formatted(id);
        }
    }

    @ShellMethod(value = "Find all comments by book id", key = "cbbid")
    public String findCommentsByBookId(long bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String text, long bookId) {
        try {
            var savedComment = commentService.create(text, bookId);
            return commentConverter.commentToString(savedComment);
        } catch (Exception e) {
            return "Error creating comment: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String text) {
        try {
            var savedComment = commentService.update(id, text);
            return commentConverter.commentToString(savedComment);
        } catch (Exception e) {
            return "Error updating comment: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public String deleteComment(long id) {
        try {
            commentService.deleteById(id);
            return "Comment with id %d deleted".formatted(id);
        } catch (Exception e) {
            return "Error deleting comment: " + e.getMessage();
        }
    }
}
