package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnListOfBooks() throws Exception {
        Book book = new Book(1L, "Test Book",
                new Author(1L, "Test Author"),
                new Genre(1L, "Test Genre"),
                List.of());

        given(bookService.findAll()).willReturn(List.of(book));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void shouldReturnBookCreateForm() throws Exception {
        mockMvc.perform(get("/book/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/create"))
                .andExpect(model().attributeExists("book", "authors", "genres"));
    }

    @Test
    void shouldReturnBookEditForm() throws Exception {
        Book book = new Book(1L, "Test Book",
                new Author(1L, "Test Author"),
                new Genre(1L, "Test Genre"),
                List.of());

        given(bookService.findById(1L)).willReturn(Optional.of(book));

        mockMvc.perform(get("/book/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"))
                .andExpect(model().attributeExists("book", "authors", "genres"));
    }
}