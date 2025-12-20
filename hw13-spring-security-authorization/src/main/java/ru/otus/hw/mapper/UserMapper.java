package ru.otus.hw.mapper;

import ru.otus.hw.dto.user.UserDto;
import ru.otus.hw.model.MyUser;

import java.util.List;

public interface UserMapper {
    UserDto toDto(MyUser user);

    List<UserDto> toDtoList(List<MyUser> users);
}
