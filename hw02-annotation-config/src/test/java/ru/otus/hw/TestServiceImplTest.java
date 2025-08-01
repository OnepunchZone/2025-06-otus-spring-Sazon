package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования")
class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("корректность вывода вопросы | ответы")
    void executeTestFor_ShouldPrintQuestionsAndAnswers() {
        Student student = new Student("Erik", "Cartman");
        List<Question> questions = List.of(
                new Question("Question 1", List.of(
                        new Answer("Answer 1", true),
                        new Answer("Answer 2", false)
                ))
        );

        when(questionDao.findAll()).thenReturn(questions);
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1);

        TestResult result = testService.executeTestFor(student);
        assertNotNull(result);
        assertEquals(student, result.getStudent());

        verify(ioService, times(2)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(ioService, times(1)).printFormattedLine("%s", "Question 1");
        verify(ioService, times(1)).printFormattedLine("%d. %s", 1, "Answer 1");
        verify(ioService, times(1)).printFormattedLine("%d. %s", 2, "Answer 2");
        verify(ioService, times(1)).readIntForRangeWithPrompt(
                1, 2, "Choose and enter answer number: ",
                "Choose answer number from 1 to 2");
    }

    @Test
    @DisplayName("корректность обработки пустого списка вопросов")
    void executeTest_ShouldHandleEmptyQuestionList() {
        Student student = new Student("Rick", "Sanchez");
        when(questionDao.findAll()).thenReturn(List.of());

        TestResult result = testService.executeTestFor(student);
        assertNotNull(result);
        assertEquals(0, result.getAnsweredQuestions().size());

        verify(questionDao, times(1)).findAll();
        verify(ioService, never()).printFormattedLine(contains("Question"), any());
    }
}
