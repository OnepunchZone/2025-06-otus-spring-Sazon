package ru.otus.hw.dto.user;

import ru.otus.hw.model.UserRole;

public record UserDto(Long id, String username, UserRole role) {
}
