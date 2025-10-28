package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/create")
    public String createComment(@RequestParam("bookId") long bookId,
                                @RequestParam("text") String text) {
        commentService.create(text, bookId);
        return "redirect:/book/view/" + bookId;
    }

    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") long id,
                                @RequestParam("bookId") long bookId) {
        commentService.deleteById(id);
        return "redirect:/book/view/" + bookId;
    }
}
