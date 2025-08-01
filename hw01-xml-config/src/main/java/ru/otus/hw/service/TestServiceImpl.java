package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();

        for (Question q : questions) {
            ioService.printFormattedLine("%s", q.text());

            for (int i = 0; i < q.answers().size(); i++) {
                Answer answer = q.answers().get(i);
                ioService.printFormattedLine("%d. %s", i + 1, answer.text());
            }
            ioService.printLine("");
        }
    }
}
