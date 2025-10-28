package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/book/create")
    public String createBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "book/create";
    }

    @PostMapping("book/create")
    public String createBook(@Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             @RequestParam("authorId") long authorId,
                             @RequestParam("genreId") long genreId,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());

            return "book/create";
        }

        bookService.insert(book.getTitle(), authorId, genreId);
        return "redirect:/";
    }

    @GetMapping("/book/edit/{id}")
    public String editBookForm(@PathVariable("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "book/edit";
    }

    @PostMapping("/book/edit/{id}")
    public String editBook(@PathVariable("id") long id,
                           @Valid @ModelAttribute("book") Book book,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());

            return "book/edit";
        }

        bookService.update(id, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return "redirect:/";
    }

    @GetMapping("/book/view/{id}")
    public String viewBook(@PathVariable("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        model.addAttribute("book", book);
        return "book/view";
    }

    @GetMapping("/book/delete/{id}")
    public String confirmDeleteBook(@PathVariable("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        model.addAttribute("book", book);
        return "book/delete";
    }

    @PostMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
