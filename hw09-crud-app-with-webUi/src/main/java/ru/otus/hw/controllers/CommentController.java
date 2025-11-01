package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dtos.commentdtos.CommentCreateDto;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/create")
    public String createComment(@Valid @ModelAttribute CommentCreateDto commentCreateDto) {
        commentService.create(commentCreateDto);
        return "redirect:/book/view/" + commentCreateDto.getBookId();
    }

    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") long id,
                                @RequestParam("bookId") long bookId) {
        commentService.deleteById(id);
        return "redirect:/book/view/" + bookId;
    }
}
