package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.StudentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Тест сервиса студентов")
public class StudentServiceImplTest {

    @MockitoBean
    private LocalizedIOService ioService;

    @Autowired
    private StudentService studentService;

    @Test
    void determineCurrentStudent_ShouldReturnCorrectStudent() {
        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn("Tirion");
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn("Lanister");

        Student student = studentService.determineCurrentStudent();

        assertThat(student)
                .hasFieldOrPropertyWithValue("firstName", "Tirion")
                .hasFieldOrPropertyWithValue("lastName", "Lanister");
    }
}
