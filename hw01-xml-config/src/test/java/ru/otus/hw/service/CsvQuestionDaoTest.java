package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест CsvQuestionDao на передачу существующего и не существующего ресурса")
public class CsvQuestionDaoTest {
    @Mock
    private TestFileNameProvider testFileNameProvider;
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
    }

    @Test
    @DisplayName("Загрузка вопросов из существующего файла")
    void findAll_ShouldReturnQuestions_IfFileExists() {
        when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");

        List<Question> questions = csvQuestionDao.findAll();

        assertNotNull(questions);
        assertEquals(5, questions.size(), "Must be 5 questions");

        Question firstQuestion = questions.get(0);
        assertEquals("Is there life on Mars?", firstQuestion.text());
        assertEquals(3, firstQuestion.answers().size(), "Must be 3 answers");
    }

    @Test
    @DisplayName("Бросает исключение при отсутствии файла")
    void findAll_ShouldThrowException_IfFileNotExists() {
        when(testFileNameProvider.getTestFileName()).thenReturn("non-existent-file.csv");

        Exception exception = assertThrows(QuestionReadException.class, () -> {
            csvQuestionDao.findAll();
        });

        assertTrue(exception.getMessage().contains("File not found"));
    }

}
