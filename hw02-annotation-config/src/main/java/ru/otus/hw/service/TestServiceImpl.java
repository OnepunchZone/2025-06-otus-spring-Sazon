package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printFormattedLine("%s", question.text());
            for (int i = 0; i < question.answers().size(); i++) {
                Answer answer = question.answers().get(i);
                ioService.printFormattedLine("%d. %s", i + 1, answer.text());
            }

            int answerNumber = ioService.readIntForRangeWithPrompt(
                    1,
                    question.answers().size(),
                    "Choose and enter answer number: ",
                    "Choose answer number from 1 to " + question.answers().size()
            );
            boolean isAnswerValid = question.answers().get(answerNumber - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
            ioService.printLine("");
        }
        return testResult;
    }
}
