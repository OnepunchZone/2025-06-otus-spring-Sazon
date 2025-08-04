package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class RunCommand {
    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run testing", key = {"run", "start"})
    public void runTesting() {
        testRunnerService.run();
    }
}
