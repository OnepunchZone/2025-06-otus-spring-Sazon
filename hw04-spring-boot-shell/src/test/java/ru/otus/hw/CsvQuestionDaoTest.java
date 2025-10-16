package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Тест CsvQuestionDao на передачу существующего и не существующего ресурса")
public class CsvQuestionDaoTest {
    @MockitoBean(name = "testFileNameProvider")
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

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
}
