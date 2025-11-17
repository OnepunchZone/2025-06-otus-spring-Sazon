package ru.otus.hw.dtos.userdtos;

import ru.otus.hw.models.UserRole;

public record UserDto(Long id, String username, UserRole role) {
}
