package ru.otus.hw.dto.user;

import jakarta.validation.constraints.NotBlank;
import ru.otus.hw.model.UserRole;

public record UserRegistrationDto(
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String username,

        @NotBlank(message = "Пароль не может быть пустым")
        String password,

        UserRole role) {

        public UserRegistrationDto {
                if (role == null) {
                        role = UserRole.USER;
                }
        }
}
