package ru.otus.hw.mappers;

import ru.otus.hw.dtos.userdtos.UserDto;
import ru.otus.hw.models.MyUser;

import java.util.List;

public interface UserMapper {
    UserDto toDto(MyUser user);

    List<UserDto> toDtoList(List<MyUser> users);
}
