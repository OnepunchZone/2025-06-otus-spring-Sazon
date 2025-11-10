package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.CommentRepository;

@Component
@RequiredArgsConstructor
public class BookDeleteEventHandler {
    private final CommentRepository cm;

    @EventListener
    public void handleBookDelete(BookDeleteEvent event) {
        cm.deleteByBookId(event.getBookId());
    }
}
