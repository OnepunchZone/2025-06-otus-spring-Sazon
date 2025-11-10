package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.dtos.bookdtos.BookCreateDto;
import ru.otus.hw.dtos.bookdtos.BookDto;
import ru.otus.hw.dtos.bookdtos.BookUpdateDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private final BookDto bookDto = new BookDto(1L, "Test Book",
            new AuthorDto(1L, "Test Author"),
            new GenreDto(1L, "Test Genre"),
            List.of());

    @Test
    void shouldReturnListOfBooks() throws Exception {

        BookDto book = new BookDto(1L, "Test Book",
                new AuthorDto(1L, "Test Author"),
                new GenreDto(1L, "Test Genre"),
                List.of());

        given(bookService.findAll()).willReturn(List.of(book));


        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Book"))
                .andExpect(jsonPath("$[0].author.fullName").value("Test Author"));
    }

    @Test
    void shouldCreateBook() throws Exception {
        BookCreateDto createDto = new BookCreateDto("New Book", 1L, 1L);
        given(bookService.create(any(BookCreateDto.class))).willReturn(bookDto);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        BookDto book = new BookDto(1L, "Test Book",
                new AuthorDto(1L, "Test Author"),
                new GenreDto(1L, "Test Genre"),
                List.of());

        given(bookService.findById(1L)).willReturn(book);

        mockMvc.perform(get("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookUpdateDto updateDto = new BookUpdateDto(1L, "Updated Book", 1L, 1L);
        BookDto updatedBook = new BookDto(1L, "Updated Book",
                new AuthorDto(1L, "Test Author"),
                new GenreDto(1L, "Test Genre"),
                List.of());

        given(bookService.update(any(BookUpdateDto.class))).willReturn(updatedBook);

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldHandleEntityNotFoundException() throws Exception {
        given(bookService.findById(99L))
                .willThrow(new EntityNotFoundException("Book with id 99 not found"));

        mockMvc.perform(get("/api/v1/books/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Book with id 99 not found"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {

        mockMvc.perform(get("/api/v1/books/invalid"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void shouldHandleIllegalArgumentException() throws Exception {
        given(bookService.findById(-1L))
                .willThrow(new IllegalArgumentException("Invalid book ID format"));

        mockMvc.perform(get("/api/v1/books/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid book ID format"));
    }
}