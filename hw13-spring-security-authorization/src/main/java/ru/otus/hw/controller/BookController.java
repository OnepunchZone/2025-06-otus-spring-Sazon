package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.book.BookCreateDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookUpdateDto;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/list";
    }

    @GetMapping("/book/create")
    public String createBookForm(Model model) {
        model.addAttribute("book", new BookCreateDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "book/create";
    }

    @PostMapping("book/create")
    public String createBook(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());

            return "book/create";
        }

        bookService.create(bookCreateDto);
        return "redirect:/";
    }

    @GetMapping("/book/edit/{id}")
    public String editBookForm(@PathVariable("id") long id, Model model) {
        var bookDto = bookService.findById(id);

        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "book/edit";
    }

    @PostMapping("/book/edit/{id}")
    public String editBook(@PathVariable("id") long id,
                           @Valid @ModelAttribute("book") BookUpdateDto bookUpdateDto,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());

            return "book/edit";
        }

        bookUpdateDto.setId(id);
        bookService.update(bookUpdateDto);
        return "redirect:/";
    }

    @GetMapping("/book/view/{id}")
    public String viewBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);

        model.addAttribute("book", book);
        return "book/view";
    }

    @GetMapping("/book/delete/{id}")
    public String confirmDeleteBook(@PathVariable("id") long id, Model model) {
        var bookDto = bookService.findById(id);

        if (bookDto == null) {
            model.addAttribute("error", "Book not found: " + id);
            return "redirect:/";
        }

        model.addAttribute("book", bookDto);
        return "book/delete";
    }

    @PostMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
