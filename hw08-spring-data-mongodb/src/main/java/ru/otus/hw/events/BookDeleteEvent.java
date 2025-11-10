package ru.otus.hw.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookDeleteEvent extends ApplicationEvent {
    private final String bookId;

    public BookDeleteEvent(Object source, String bookId) {
        super(source);
        this.bookId = bookId;
    }
}
