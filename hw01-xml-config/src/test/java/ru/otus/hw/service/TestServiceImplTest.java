package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

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
    void executeTest_ShouldPrintQuestionsAndAnswers() {
        List<Question> questions = List.of(
                new Question("Question 1", List.of(
                        new Answer("Answer 1", true),
                        new Answer("Answer 2", false)
                ))
        );

        when(questionDao.findAll()).thenReturn(questions);

        testService.executeTest();

        verify(ioService, times(2)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(ioService, times(1)).printFormattedLine("%s", "Question 1");
        verify(ioService, times(1)).printFormattedLine("%d. %s", 1, "Answer 1");
        verify(ioService, times(1)).printFormattedLine("%d. %s", 2, "Answer 2");
    }

    @Test
    @DisplayName("корректность обработки пустого списока вопросов")
    void executeTest_ShouldHandleEmptyQuestionList() {
        when(questionDao.findAll()).thenReturn(List.of());

        testService.executeTest();

        verify(questionDao, times(1)).findAll();
        verify(ioService, never()).printFormattedLine(anyString(), anyString());
    }
}
