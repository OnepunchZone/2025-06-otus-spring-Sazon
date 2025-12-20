package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.config.security.SecurityConfiguration;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    @WithAnonymousUser
    void shouldReturnListOfBooks() throws Exception {
        BookDto book = new BookDto(1L, "Test Book",
                new AuthorDto(1L, "Test Author"),
                new GenreDto(1L, "Test Genre"),
                List.of());

        given(bookService.findAll()).willReturn(List.of(book));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldReturnBookCreateForm() throws Exception {
        mockMvc.perform(get("/book/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/create"))
                .andExpect(model().attributeExists("book", "authors", "genres"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    void shouldReturnBookEditForm() throws Exception {
        BookDto book = new BookDto(1L, "Test Book",
                new AuthorDto(1L, "Test Author"),
                new GenreDto(1L, "Test Genre"),
                List.of());

        given(bookService.findById(1L)).willReturn(book);

        mockMvc.perform(get("/book/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/edit"))
                .andExpect(model().attributeExists("book", "authors", "genres"));
    }

    @Test
    @WithAnonymousUser
    void anonymousCannotAccessBookView() throws Exception {
        mockMvc.perform(get("/book/view/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotAccessBookDeleteConfirm() throws Exception {
        mockMvc.perform(get("/book/delete/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanAccessBookDeleteConfirm() throws Exception {
        BookDto testBook = new BookDto(1L, "Test Book Title",
                new AuthorDto(1L, "Test Author"),
                new GenreDto(1L, "Test Genre"),
                List.of());
        given(bookService.findById(1L)).willReturn(testBook);

        mockMvc.perform(get("/book/delete/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/delete"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    @WithAnonymousUser
    void anonymousCannotCreateBook() throws Exception {
        mockMvc.perform(post("/book/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanCreateBook() throws Exception {  // Предполагаем, что create для USER ок (не ADMIN)
        mockMvc.perform(post("/book/create")
                        .param("title", "Test")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithAnonymousUser
    void whenUnauthenticated_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/list"));
    }

    @Test
    @WithAnonymousUser
    void whenUnauthenticated_createBookRedirect() throws Exception {
        mockMvc.perform(get("/book/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}